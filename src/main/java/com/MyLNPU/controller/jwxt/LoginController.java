package com.MyLNPU.controller.jwxt;

import com.MyLNPU.model.responseModel.Response;
import com.MyLNPU.service.jwxt.LoginService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/jwxt")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping("/login")
    public String login(@RequestParam Map<String,Object> DataForm) throws Exception {
        Response cookie = loginService.login(DataForm.get("userAccount").toString(), DataForm.get("userPassword").toString());
        return JSON.toJSONString(cookie);
    }
}
