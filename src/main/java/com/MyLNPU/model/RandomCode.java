package com.MyLNPU.model;

public class RandomCode {
    private String code;
    private String cookie;

    public void setCode(String code) {
        this.code = code;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCode() {
        return code;
    }

    public String getCookie() {
        return cookie;
    }

    public RandomCode(String code, String cookie) {
        this.code = code;
        this.cookie = cookie;
    }
}
