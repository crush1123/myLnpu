package com.MyLNPU.service.jwxt;

import com.MyLNPU.controller.utils.CookieException;
import com.MyLNPU.model.jwxt.ScoreModel;
import com.MyLNPU.model.responseModel.Data;
import com.MyLNPU.model.responseModel.Response;
import org.springframework.stereotype.Service;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Service
public class ScoreService {
    public Response getScores(String cookie) throws Exception {
        Data data = new Data();
        List<ScoreModel> scoreList = new ArrayList<>();
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet Info_Get = new HttpGet("https://jwxt.lnpu.edu.cn/jsxsd/kscj/cjcx_list");
        Info_Get.setHeader("cookie", cookie);
        CloseableHttpResponse response = client.execute(Info_Get);
        InputStream inputStream = response.getEntity().getContent();
        Document document = Jsoup.parse(inputStream, "UTF-8", "");
        Elements elements = document.body().select(".Nsb_r_list tr");
        if (elements.size() == 0) {
            client.close();
            throw new CookieException("当前Cookie已失效");
        }
        elements.remove(0);
        for (Element element : elements) {
            List<String> list = new ArrayList<>();
            for (Element child : element.children()) {
                String str = child.text().trim();
                list.add(str);
            }
            ScoreModel score = new ScoreModel();
            score.setTerm(list.get(1));
            score.setClassName(list.get(3));
            score.setScore(list.get(5));
            score.setCredits(list.get(7));
            score.setGPA(list.get(9));
            score.setType(list.get(14));
            scoreList.add(score);
        }
        client.close();
        data.setScore(scoreList);
        return new Response(1, "获取成绩信息成功", data);
    }
}
