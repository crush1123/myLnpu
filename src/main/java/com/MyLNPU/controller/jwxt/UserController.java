package com.MyLNPU.controller.jwxt;

import com.MyLNPU.model.responseModel.Response;
import com.MyLNPU.service.jwxt.UserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jwxt")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/user")
    public String getUser(@RequestParam("cookie") String cookie) throws Exception {
        Response info = userService.getInfo(cookie);
        return JSON.toJSONString(info);
    }
}
