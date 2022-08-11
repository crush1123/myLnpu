package com.MyLNPU.model.responseModel;

import com.MyLNPU.model.jwxt.CourseModel;
import com.MyLNPU.model.ExperimentModel;
import com.MyLNPU.model.jwxt.ScoreModel;
import com.MyLNPU.model.jwxt.UserModel;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Data {
    @JSONField(name = "cookie")
    private String cookie;
    @JSONField(name = "user")
    private UserModel user;
    @JSONField(name = "table")
    private List<CourseModel> table;
    @JSONField(name = "score")
    private List<ScoreModel> score;
    @JSONField(name = "currentWeek")
    private String currentWeek;
    @JSONField(name = "experiment")
    private List<ExperimentModel> experiment;
    @JSONField(name = "currentTerm")
    private String currentTerm;
    @JSONField(name = "startTime",format="yyyy-MM-dd")
    private Date startTime;
    public Data() {
    }
}
