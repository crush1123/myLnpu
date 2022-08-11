package com.MyLNPU.model.responseModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ResEntity {
    @JSONField(name = "flag")
    private String flag;
    @JSONField(name = "msgCode")
    private String msgCode;
    @JSONField(name = "msg")
    private String msg;
}
