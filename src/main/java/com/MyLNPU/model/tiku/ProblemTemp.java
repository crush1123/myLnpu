package com.MyLNPU.model.tiku;

import lombok.Data;

import java.util.List;

@Data
public class ProblemS {
    private String title;
    private int type;
    List<String> options;
    List<String> answers;
}
