package com.MyLNPU.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ExperimentModel {
    @JSONField(name = "status")
    private String status;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "teacher")
    private String teacher;
    @JSONField(name = "address")
    private String address;
    @JSONField(name = "date")
    private String date;
    @JSONField(name = "section")
    private String section;
    @JSONField(name = "week")
    private String week;
}
