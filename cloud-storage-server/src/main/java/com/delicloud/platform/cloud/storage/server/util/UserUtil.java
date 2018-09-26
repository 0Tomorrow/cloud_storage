package com.delicloud.platform.cloud.storage.server.util;

public class UserUtil {
    public static String verifyParam(String paramName, Object param) {
        if (paramName.equals("account") && !((Long)param).toString().matches("1\\d{10}")) {
            return "手机号格式错误";
        } else if (paramName.equals("password") && !((String)param).matches("\\w{6,10}")) {
            return "密码格式错误";
        }
        else {
            return null;
        }
    }
}
