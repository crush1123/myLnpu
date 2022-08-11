package com.MyLNPU.model.classRoom;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WeekModul implements Serializable {
        private int weeks;
        private List<Integer> jc;
}
