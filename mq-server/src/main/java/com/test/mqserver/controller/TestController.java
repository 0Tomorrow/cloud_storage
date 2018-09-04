package com.test.mqserver.controller;

import com.test.mqserver.test.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    Test test;

    @RequestMapping("test")
    public void test() {
        test.test();
        System.out.println("+++++++");
    }
}
