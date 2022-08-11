package com.MyLNPU.controller.Experiment;

import com.MyLNPU.model.responseModel.Response;
import com.MyLNPU.service.experiment.ExperimentLoginService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/sy")
public class ExperimentLoginController {
    @Autowired
    private ExperimentLoginService loginService;
    @PostMapping("/login")
    public String login(@RequestParam Map<String,Object> DataForm) throws Exception {
        Response login = loginService.login(DataForm.get("teaId").toString(), DataForm.get("teaPwd").toString());
        return JSON.toJSONString(login);
    }
}
