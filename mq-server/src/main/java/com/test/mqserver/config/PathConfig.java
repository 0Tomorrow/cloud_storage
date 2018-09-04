package com.test.mqserver.config;

public class PathConfig {
    public static String getPath(Long userAccount, String relativePath) {
        if(!relativePath.equals("")) {
            relativePath = "/" + relativePath;
        }
        relativePath = userAccount + relativePath;
        return "E://test/" + relativePath;
    }
}
