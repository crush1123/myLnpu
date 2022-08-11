package com.MyLNPU.controller.classRoom;

import com.MyLNPU.model.responseModel.R;
import com.MyLNPU.service.GetClassRoomService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/api")
public class GetClassRoomController {
    @Autowired
    GetClassRoomService getClassRoom;

    @GetMapping("/getclassroom")
    public String getAllClassRoom(@RequestParam("cookie") String cookie, @RequestParam("term") String term,@RequestParam("zc") String zc,@RequestParam("userAccount") String userAccount) throws IOException, URISyntaxException {
        log.info("开始获取");
        System.out.println("cookie"+cookie);
        System.out.println("term"+term);
        System.out.println("zc"+zc);
        R classRoom = getClassRoom.getClassRoom(cookie,term,zc,userAccount);
        return JSON.toJSONString(classRoom);
    }

}
