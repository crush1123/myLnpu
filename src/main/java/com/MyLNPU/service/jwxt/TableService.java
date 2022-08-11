package com.MyLNPU.service.jwxt;

import com.MyLNPU.controller.utils.CookieException;
import com.MyLNPU.model.jwxt.CourseModel;
import com.MyLNPU.model.responseModel.Data;
import com.MyLNPU.model.responseModel.Response;
import com.MyLNPU.utils.Parse;
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
import java.util.*;

@Service
public class TableService {
    public Response getTable(String cookie) throws Exception {
        List<CourseModel> courseList = new ArrayList<>();
        Data data = new Data();
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet Info_Get = new HttpGet("https://jwxt.lnpu.edu.cn/jsxsd/xskb/xskb_list.do");
        Info_Get.setHeader("cookie", cookie);
        CloseableHttpResponse response = client.execute(Info_Get);
        InputStream inputStream = response.getEntity().getContent();
        Document document = Jsoup.parse(inputStream, "UTF-8", "");
        Elements elements = document.select(".Nsb_table tr");
        if (elements.size() == 0) {
            client.close();
            throw new CookieException("当前Cookie已失效");
        }
        elements.remove(0);
        elements.select(".sykb2").remove();
        elements.select(".sykb1").remove();
        elements.select(".kbcontent1").remove();
        elements.select("input").remove();
        for (int i = 0; i < elements.size(); i++) {
            int sections = i + 1;
            Element element = elements.get(i);
            Elements courses = element.select(".kbcontent");
            for (int j = 0; j < courses.size(); j++) {
                int week = j + 1;
                Element course = courses.get(j);
                if (!course.ownText().equals("")) {
                    Elements properties = course.children();
                    Set<String> set = new LinkedHashSet<>();
                    String[] name = course.ownText().split(" ");
                    for (String s : name) {
                        set.add(s);
                    }
                    Object[] array = set.toArray();
                    Elements teacher_title = properties.select("[title='老师']");
                    Elements weekTime_title = properties.select("[title='周次(节次)']");
                    String[] weekTime=weekTime_title.text().split(" ");
                    System.out.println(weekTime.length);
                    Elements address_title = properties.select("[title='教室']");
                    CourseModel courseModel = new CourseModel();
                    courseModel.setCourseName(array[0].toString());
                    courseModel.setWeek(week);
                    courseModel.setSections(sections);
                    List<Integer> lists=new ArrayList<>();
                    for (int k = 0; k < weekTime.length; k++) {
                        if (address_title.size() != 0) {
                            courseModel.setAddress(address_title.get(k).text());
                        }
                        if (teacher_title.size() != 0) {
                            courseModel.setTeacherName(teacher_title.get(k).text());
                        }
                        List<Integer> list = Parse.parseToArray(weekTime_title.get(k).text());
                        lists.addAll(list);
                    }
                    Collections.sort(lists);
                    courseModel.setWeekTimes(lists);
                    courseList.add(courseModel);
                }
            }
        }
        client.close();
        System.out.println(courseList);
        data.setTable(courseList);
        return new Response(1, "获取课表信息成功", data);
    }
}
