package com.MyLNPU.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 解析周次
 */
public class Parse {
    private static List<Integer> parseWeeks(String weeks){
        List<Integer> list = new ArrayList<>();
        if(weeks.length()==1){
            list.add(Integer.parseInt(weeks));
        }else {
            String[] split = weeks.split(",");
            for (String s : split) {
                if(s.length() < 3){
                    if (s.trim().equals("")){
                        continue;
                    }
                    list.add(Integer.parseInt(s));
                    continue;
                }
                String[] index = s.split("-");
                for(int i=Integer.parseInt(index[0]);i<=Integer.parseInt(index[1]);i++){
                    list.add(i);
                }
            }
        }

        Collections.sort(list);
        return list;
    }
    public static List<Integer> parseToArray(String str){
        String[] split = str.split("\\(周\\)");
        List<Integer> weeks = parseWeeks(split[0]);
        return weeks;
    }
}
