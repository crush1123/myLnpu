package com.MyLNPU.model.jwxt;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class UserModel {
    @JSONField(name = "id")
    private String id;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "college")
    private String college;
    @JSONField(name = "major")
    private String major;
    @JSONField(name = "classes")
    private String classes;
}
