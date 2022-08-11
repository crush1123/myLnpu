package com.MyLNPU.controller.classRoom;

import com.MyLNPU.model.responseModel.R;
import com.MyLNPU.service.ClassRoomService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/jwxt")
public class ClassRoomController {
    @Autowired
    ClassRoomService classRoomService;

    @GetMapping("/classroom")
    public String getClassRoom(@RequestParam("name") String name,@RequestParam("start") int start,@RequestParam("pageSize") int pageSize,@RequestParam("userAccount") String userAccount)  {
       log.info("名称"+name+"开始："+start+"页数"+pageSize);
        R allClassRoom = classRoomService.getAllClassRoom(name, start, pageSize,userAccount);
        return JSON.toJSONString(allClassRoom);
    }

    @GetMapping("/classroom/{week}")
    public String getClassRoomByWeek(@RequestParam("name") String name,@RequestParam("start") int start,@RequestParam("pageSize") int pageSize,@PathVariable("week") int week,@RequestParam("userAccount") String userAccount){
        log.info("名称"+name+"开始："+start+"页数"+pageSize);
        R classRoomByWeek = classRoomService.getClassRoomByWeek(name, start, pageSize, week,userAccount);
        return JSON.toJSONString(classRoomByWeek);
    }
}
