package com.MyLNPU.model.responseModel;


import lombok.Data;

@Data
public class R<T> {
    private int code;
    private Object data;
    private String message;

    public static R error(String message){
        R res = new R<>();
        res.setCode(-1);
        res.setMessage(message);
        return res;
    }

    public static R success(){
        R res = new R<>();
        res.setCode(1);
        return res;
    }

    public static R success(String message){
        R res = new R<>();
        res.setCode(1);
        res.setMessage(message);
        return res;
    }

    public R setData(Object data) {
        this.data = data;
        return this;
    }

}
