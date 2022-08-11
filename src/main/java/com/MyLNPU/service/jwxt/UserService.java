package com.MyLNPU.service.jwxt;

import com.MyLNPU.controller.utils.CookieException;
import com.MyLNPU.model.jwxt.UserModel;
import com.MyLNPU.model.responseModel.Data;
import com.MyLNPU.model.responseModel.Response;
import org.springframework.stereotype.Service;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    public Response getInfo(String cookie) throws Exception {
        Data data = new Data();
        UserModel user = new UserModel();
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet Info_Get = new HttpGet("https://jwxt.lnpu.edu.cn/jsxsd/framework/xsMain_new.jsp");
        Info_Get.setHeader("cookie", cookie);
        CloseableHttpResponse response = client.execute(Info_Get);
        InputStream inputStream = response.getEntity().getContent();
        Document document = Jsoup.parse(inputStream, "UTF-8", "");
        Elements elements = document.select(".middletopdwxxcont");
        if (elements.size() == 0) {
            client.close();
            throw new CookieException("当前Cookie已失效");
        }
        Elements select = document.select(".main_text");
        String weekText = select.get(0).text();
        data.setCurrentWeek(weekText.substring(1,weekText.length()-1));
        elements.remove(0);
        user.setName(elements.get(0).text());
        user.setId(elements.get(1).text());
        user.setCollege(elements.get(2).text());
        user.setMajor(elements.get(3).text());
        user.setClasses(elements.get(4).text());
        data.setUser(user);
        HttpGet Time_Get = new HttpGet("https://jwxt.lnpu.edu.cn/jsxsd/jxzl/jxzl_query");
        Time_Get.setHeader("cookie",cookie);
        CloseableHttpResponse response1 = client.execute(Time_Get);
        Map<String, Object> time = getStartTime(response1.getEntity().getContent());
        data.setCurrentTerm(time.get("currentTerm").toString());
        data.setStartTime((Date)time.get("startTime"));
        client.close();
        return new Response(1, "获取用户信息成功", data);
    }
    private Map<String,Object> getStartTime(InputStream inputStream) throws Exception {
        Map<String,Object> map = new HashMap<>();
        Document parse = Jsoup.parse(inputStream, "UTF-8", "");
        Elements select = parse.select(".Nsb_r_list tr");
        select.remove(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd");
        Date startTime = simpleDateFormat.parse(select.get(0).children().get(1).attr("title"));
        Elements currentTerm = parse.select("option").select("[selected=selected]");
        map.put("startTime",startTime);
        map.put("currentTerm",currentTerm.text());
        return map;
    }
}
