package com.delicloud.platform.cloud.storage.server;

import com.delicloud.platform.cloud.storage.server.file.FileUtil;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject("http://192.168.0.202:8088/app/mock/34/getVersion?type=12321321321321421", String.class);
//        System.out.println(result);

        System.out.println(FileUtil.getFileMd5("E:/tomcat/apache-tomcat-8.5.34/webapps/ROOT/cloud_storage/user_file/15927860204/第六周（田林枫）(1).pdf"));
        System.out.println(FileUtil.getFileMd5("E:/tomcat/apache-tomcat-8.5.34/webapps/ROOT/cloud_storage/user_file/15927860204/第六周（田林枫）(2).pdf"));

    }
}
