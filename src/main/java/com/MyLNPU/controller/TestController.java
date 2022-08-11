package com.MyLNPU.controller;

import com.MyLNPU.model.classRoom.ClassRoomModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("api/jwxt")
public class TestController {
    @Resource
    RedisTemplate<String, ClassRoomModel> redisTemplate;

    @GetMapping("/test")
    public String test(@RequestParam("start") int start,@RequestParam("pageSize") int pageSzie){
        List<ClassRoomModel> range = redisTemplate.opsForList().range("教一", start, start+pageSzie-1);
        System.out.println(range.size());
        System.out.println(range);
        return "success";
    }

    @GetMapping("/test1")
    public String test1(@RequestParam("start") int start,@RequestParam("pageSize") int pageSzie){
        List<ClassRoomModel> range = (List<ClassRoomModel>) redisTemplate.opsForHash().get("2012200107", "教一");
        List<ClassRoomModel> classRoomModels = range.subList(start, pageSzie);
        System.out.println(classRoomModels);
        return "success";
    }
}
