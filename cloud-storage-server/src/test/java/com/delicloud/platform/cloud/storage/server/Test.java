package com.delicloud.platform.cloud.storage.server;

public class Test {
    public static void main(String[] args) {
        String fileName = "123.123.123.exe";
        String type = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        System.out.println(type);
    }
}
