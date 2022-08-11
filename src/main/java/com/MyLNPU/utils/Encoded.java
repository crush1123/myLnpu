package com.MyLNPU.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 账号密码加密
 */
public class Encoded {
    public static String encode(String username, String password) {
        username = Base64.getEncoder().encodeToString(username.getBytes(StandardCharsets.UTF_8));
        password = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
        String encoded = username + "%%%" + password;
        System.out.println("账号密码的base64加密值为：" + encoded);
        return encoded;
    }
}