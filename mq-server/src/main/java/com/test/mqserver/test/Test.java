package com.test.mqserver.test;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class Test {
    @Async
    public synchronized void test() {
        for (int i = 0; i < 200; i++) {
            System.out.println(i);
        }
    }
}
