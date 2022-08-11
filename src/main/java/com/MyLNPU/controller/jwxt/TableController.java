package com.MyLNPU.controller.jwxt;

import com.MyLNPU.model.responseModel.Response;
import com.MyLNPU.service.jwxt.TableService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jwxt")
public class TableController {
    @Autowired
    private TableService tableService;
    @PostMapping("/table")
    public String getTable(@RequestParam("cookie") String cookie) throws Exception {
        Response table = tableService.getTable(cookie);
        return JSON.toJSONString(table);
    }
}
