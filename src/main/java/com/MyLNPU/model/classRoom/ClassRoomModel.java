package com.MyLNPU.model.classRoom;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ClassRoomModel implements Serializable {
    private String room;
    private List<WeekModul> week;
    private String zc;
}



