package com.MyLNPU.controller.jwxt;

import com.MyLNPU.model.responseModel.Response;
import com.MyLNPU.service.jwxt.ScoreService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jwxt")
public class ScoreController {
    @Autowired
    private ScoreService scoreService;
    @PostMapping("/scores")
    public String getScores(@RequestParam("cookie") String cookie) throws Exception {
        Response scores = scoreService.getScores(cookie);
        return JSON.toJSONString(scores);
    }
}
