package com.MyLNPU.controller.Experiment;

import com.MyLNPU.model.responseModel.Response;
import com.MyLNPU.service.experiment.ExperimentTableService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sy")
public class ExperimentTableController {
    @Autowired
    private ExperimentTableService experimentTableService;
    @PostMapping("/table")
    public String getTable(@RequestParam("cookie") String cookie) throws Exception {
        Response experiments = experimentTableService.getExperiments(cookie);
        return JSON.toJSONString(experiments);
    }
}
