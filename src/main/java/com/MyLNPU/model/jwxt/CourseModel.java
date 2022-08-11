package com.MyLNPU.model.jwxt;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class CourseModel {
    @JSONField(name = "courseName")
    private String courseName;
    @JSONField(name = "weekTimes")
    private List<Integer> weekTimes;
    @JSONField(name = "week")
    private int week;
    @JSONField(name = "address")
    private String address;
    @JSONField(name = "teacher")
    private String teacherName;
    @JSONField(name = "sections")
    private int sections;
}
