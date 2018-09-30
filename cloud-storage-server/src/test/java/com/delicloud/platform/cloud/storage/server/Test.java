package com.delicloud.platform.cloud.storage.server;

import org.springframework.web.client.RestTemplate;

public class Test {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("http://192.168.0.202:8088/app/mock/34/getVersion?type=12321321321321421", String.class);
        System.out.println(result);
    }
}
