package com.MyLNPU.service;


import com.MyLNPU.model.classRoom.ClassRoomModel;
import com.MyLNPU.model.classRoom.WeekModul;
import com.MyLNPU.model.responseModel.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ClassRoomService {

    @Autowired
    RedisTemplate redisTemplate;

    public R getAllClassRoom(String name, int start, int pageSize, String userAccount) {
        Map<String, Object> result = new HashMap<>();
        List<ClassRoomModel> range = (List<ClassRoomModel>) redisTemplate.opsForHash().get(userAccount, name);
        if (range != null) {
            if (start<=range.size()){
                if(start+pageSize<=range.size()){
                    List<ClassRoomModel> classRoomModels = range.subList(start, start+pageSize);
                    result.put("classRoom", classRoomModels);
                }else {
                    List<ClassRoomModel> classRoomModels = range.subList(start, range.size());
                    result.put("classRoom", classRoomModels);
                }
            }else {
                result.put("classRoom","");
            }
            return R.success("获取空教室成功!").setData(result);
        }else {
            return R.error("本地没有缓存");
        }

    }

    public R getClassRoomByWeek(String name, int start, int pageSize, int week, String userAccount) {
        Map<String, Object> result = new HashMap<>();
        List<ClassRoomModel> range = (List<ClassRoomModel>) redisTemplate.opsForHash().get(userAccount, name);
        if (range != null) {
            if (start<=range.size()){
                List<ClassRoomModel> ans = new ArrayList<>();
                if(start+pageSize<=range.size()){
                    List<ClassRoomModel> classRoomModels = range.subList(start, start+pageSize);
                    for (ClassRoomModel classRoomModel : classRoomModels) {
                        for (WeekModul weekModul : classRoomModel.getWeek()) {
                            int weeks = weekModul.getWeeks();
                            if (weeks == week) {
                                List<WeekModul> weekModuls = new ArrayList<>();
                                weekModuls.add(weekModul);
                                classRoomModel.setWeek(weekModuls);
                                ans.add(classRoomModel);
                            }
                        }
                    }
                    result.put("classRoom", ans);
                }else {
                    List<ClassRoomModel> classRoomModels = range.subList(start, range.size());
                    for (ClassRoomModel classRoomModel : classRoomModels) {
                        for (WeekModul weekModul : classRoomModel.getWeek()) {
                            int weeks = weekModul.getWeeks();
                            if (weeks == week) {
                                List<WeekModul> weekModuls = new ArrayList<>();
                                weekModuls.add(weekModul);
                                classRoomModel.setWeek(weekModuls);
                                ans.add(classRoomModel);
                            }
                        }
                    }
                    result.put("classRoom", ans);
                }
            }else {
                result.put("classRoom","");
            }
            return R.success("获取空教室成功!").setData(result);
        }else {
            return R.error("本地没有缓存");
        }
    }
}
