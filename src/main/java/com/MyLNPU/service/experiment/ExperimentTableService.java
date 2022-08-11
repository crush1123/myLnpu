package com.MyLNPU.service.experiment;

import com.MyLNPU.controller.utils.CookieException;
import com.MyLNPU.model.ExperimentModel;
import com.MyLNPU.model.responseModel.Data;
import com.MyLNPU.model.responseModel.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ExperimentTableService {
    public Response getExperiments(String cookie) throws Exception {
        String url = "http://202.118.120.108/student/stutimetable/list";
        String page = "?page=";
        int index = 1;
        Data data = new Data();
        List<ExperimentModel> list = new ArrayList<>();
        CloseableHttpClient client = HttpClientBuilder.create().build();
        while (true){
            HttpGet info = new HttpGet(url+page+index);
            System.out.println("正在获取第"+index+"页数据");
            info.setHeader("cookie", cookie);
            CloseableHttpResponse response = client.execute(info);
            InputStream inputStream = response.getEntity().getContent();
            Document document = Jsoup.parse(inputStream, "UTF-8", "");
            Elements select = document.select(".page_course_area");
            if (document.select(".links").size()>0){
                throw new CookieException("当前cookie已过期");
            }
            String[] split = document.select(".row").get(5).text().split(" ");
            String msg = split[split.length-1];
            if (msg.equals("目前没有数据")){
                break;
            }
            for (int i = 0; i < select.size(); i++) {
                ExperimentModel experiment = new ExperimentModel();
                Element element = select.get(i);
                Elements properties = element.children();
                Map<String, String> nameMap = parseName(properties.get(0).text());
                experiment.setName(nameMap.get("name"));
                experiment.setStatus(nameMap.get("status"));
                Map<String, String> timeMap = parseTime(properties.get(1).text());
                experiment.setDate(timeMap.get("date"));
                experiment.setWeek(timeMap.get("week"));
                experiment.setSection(timeMap.get("section"));
                experiment.setTeacher(properties.get(2).text().split("：")[1]);
                experiment.setAddress(properties.get(3).text().split("：")[1]);
                list.add(experiment);
                data.setExperiment(list);
            }
            index++;
        }
        return new Response(1,"获取实验课表成功",data);
    }
    private Map<String,String> parseName(String name){
        Map<String,String> map = new HashMap<>();
        String[] strings = name.split(" ");
        map.put("status",strings[0]);
        map.put("name",strings[1]);
        return map;
    }
    private Map<String,String> parseTime(String time){
        Map<String,String> map = new HashMap<>();
        String string = time.split("：")[1];
        String date,week,section;
        date = string.split(" ")[0];
        Pattern pattern =Pattern.compile("\\(.*\\)");
        Matcher matcher = pattern.matcher(time);
        if(matcher.find()){
            String group = matcher.group();
            String[] split = group.split("\\) \\(");
            week = split[0].substring(1);
            section = split[1].substring(0,split[1].length()-1);
            map.put("date",date);
            map.put("week",week);
            map.put("section",section);
        }
        return map;
    }
}
