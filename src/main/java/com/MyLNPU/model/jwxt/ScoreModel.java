package com.MyLNPU.model.jwxt;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ScoreModel {
    @JSONField(name = "term")
    private String term;
    @JSONField(name = "className")
    private String className;
    @JSONField(name = "score")
    private String score;
    @JSONField(name = "GPA")
    private String GPA;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "credits")
    private String credits;
}
