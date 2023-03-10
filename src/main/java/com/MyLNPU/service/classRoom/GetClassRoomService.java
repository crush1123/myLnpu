package com.MyLNPU.service;


import com.MyLNPU.model.classRoom.ClassRoomModel;
import com.MyLNPU.model.classRoom.WeekModul;
import com.MyLNPU.model.responseModel.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class GetClassRoomService {

    @Autowired
    RedisTemplate redisTemplate;

    public R getClassRoom(String cookie, String term,String zc,String userAccount) throws IOException, URISyntaxException {
        log.info("开始访问教务处");
        CloseableHttpClient client = HttpClientBuilder.create().build();
        URI uri = new URIBuilder("https://jwxt.lnpu.edu.cn/jsxsd/kbxx/jsjy_query2")
                .addParameter("xnxqh", term)
                .addParameter("typewhere", "jszq")
                .addParameter("kbjcmsid", "9486203B90F3E3CBE0532914A8C03BE2")
                .addParameter("xqbh", "0")
                .addParameter("bjfh", "=")
                .addParameter("jszt", "5")
                .addParameter("zc", zc)
                .addParameter("zc2", zc).build();
        HttpGet Info_Get = new HttpGet(uri);
        Info_Get.setHeader("cookie", cookie);
        CloseableHttpResponse res = client.execute(Info_Get);
        InputStream inputStream = res.getEntity().getContent();
        Document document = Jsoup.parse(inputStream, "UTF-8", "");
        Elements title = document.select("title");
        if (!title.isEmpty() && title.text().equals("登录")) {
            return R.error("请登录！");
        }
        redisTemplate.delete(userAccount);
        List<String> roomList =new ArrayList<>();
        List<List<String>> typeList =new ArrayList<>();
        Elements elements = document.select("tr[jsbh]");
        System.out.println("所有数据");
        for (Element element : elements) {
            String room = element.text().split(" ")[0];
            int xian = room.indexOf("线上授课");
            int yu = room.indexOf("瑜伽");
            int zhu = room.indexOf("足");
            int yumao = room.indexOf("羽毛球");
            int tai = room.indexOf("跆拳道");
            int hu = room.indexOf("琥珀煤精工作室");
            int gan = room.indexOf("橄榄球");
            int you = room.indexOf("游泳");
            int lan = room.indexOf("篮");
            int tiyu = room.indexOf("体育");
            int pai = room.indexOf("排");
            int wang = room.indexOf("网");
            int wusu = room.indexOf("武术");
            int ping = room.indexOf("乒乓");
            int gongzuo = room.indexOf("室");
            int qiu = room.indexOf("球");
            int guan = room.indexOf("馆");
            int suzi = room.indexOf("素质拓展基地");
            int tushu = room.indexOf("图书馆");
            if (xian == -1 && yu == -1 && zhu == -1 && yumao == -1 && tai == -1 && hu == -1 && gan == -1 && you == -1
                    && lan == -1 && tiyu == -1 && pai == -1 && wang == -1 && wusu == -1 && ping == -1 && gongzuo == -1
                    && qiu == -1 && guan == -1 && suzi == -1 && tushu == -1
            ) {
                String classroom = room.split("\\(")[0];
                roomList.add(classroom);
                Elements select = element.select("td[^align]");
                List<String> temp = new ArrayList<>();
                for (Element e : select) {
                    if (e.text().isEmpty()) {
                        temp.add("E");
                    } else {
                        temp.add("F");
                    }
                }
                typeList.add(temp);
            }
        }

        List<ClassRoomModel> classRoomModels =new ArrayList<>();
        for (int i=0;i< roomList.size();i++) {
            String room =roomList.get(i);
            ClassRoomModel classRoomModel =new ClassRoomModel();
            classRoomModel.setRoom(room);
            classRoomModel.setZc(zc);
            List<String> type = typeList.get(i);
            List<WeekModul> weekModuls =new ArrayList<>();
            List<List<String>> week =new ArrayList<>();
            for (int j=0;j<type.size();j+=6){
                if (j + 6 <= type.size()) {
                    week.add(type.subList(j, j + 6));
                }
            }
            for (int j=0;j<week.size();j++){
                List<String> list2 = week.get(j);
                List<Integer> jc =new ArrayList<>();
                for (int k=0;k<list2.size();k++){
                    if (list2.get(k).equals("E")){
                        jc.add(k+1);
                    }
                }
                if (!jc.isEmpty()){
                    WeekModul weekModul =new WeekModul();
                    weekModul.setWeeks(j+1);
                    weekModul.setJc(jc);
                    weekModuls.add(weekModul);
                }
            }
            classRoomModel.setWeek(weekModuls);
            classRoomModels.add(classRoomModel);
        }

        List<ClassRoomModel> j1 =new ArrayList<>();
        List<ClassRoomModel> j2 =new ArrayList<>();
        List<ClassRoomModel> j3 =new ArrayList<>();
        List<ClassRoomModel> j4 =new ArrayList<>();
        List<ClassRoomModel> j5 =new ArrayList<>();
        List<ClassRoomModel> jxl =new ArrayList<>();
        List<ClassRoomModel> jf =new ArrayList<>();
        List<ClassRoomModel> waiyulou =new ArrayList<>();
        List<ClassRoomModel> other =new ArrayList<>();

        HashOperations hashOperations = redisTemplate.opsForHash();
        for (ClassRoomModel classRoomModel : classRoomModels) {
            if (classRoomModel.getRoom().contains("教一")) {
                j1.add(classRoomModel);
            } else if (classRoomModel.getRoom().contains("教二")) {
                j2.add(classRoomModel);
            } else if (classRoomModel.getRoom().contains("教三")) {
                j3.add(classRoomModel);
            } else if (classRoomModel.getRoom().contains("教四")) {
                j4.add(classRoomModel);
            } else if (classRoomModel.getRoom().contains("教五")) {
                j5.add(classRoomModel);
            } else if (classRoomModel.getRoom().contains("机械楼")) {
                jxl.add(classRoomModel);
            } else if (classRoomModel.getRoom().contains("机房")) {
                jf.add(classRoomModel);
            } else if (classRoomModel.getRoom().contains("外语楼")) {
                waiyulou.add(classRoomModel);
            }else {
                other.add(classRoomModel);
            }
        }
        hashOperations.put(userAccount,"教一",j1);
        hashOperations.put(userAccount,"教二",j2);
        hashOperations.put(userAccount,"教三",j3);
        hashOperations.put(userAccount,"教四",j4);
        hashOperations.put(userAccount,"教五",j5);
        hashOperations.put(userAccount,"机械楼",jxl);
        hashOperations.put(userAccount,"机房",jf);
        hashOperations.put(userAccount,"外语楼",waiyulou);
        hashOperations.put(userAccount,"其他",other);
        redisTemplate.expire(userAccount,30, TimeUnit.MINUTES);

        return R.success("添加空教室到数据库成功");
    }
}