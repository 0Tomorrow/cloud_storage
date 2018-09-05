package com.test.mqserver.test;

import org.springframework.stereotype.Component;

@Component
public class Test {
    public static void main(String[] args) {
//        TestEnum testEnum = TestEnum.getByUrl("http://delicloud.cn/barcode?type=user&user_id=12345");
//        System.out.println(testEnum);

        String clazz = Thread.currentThread() .getStackTrace()[1].getClassName();
        String method = Thread.currentThread() .getStackTrace()[1].getMethodName();
        System.out.println(clazz);
        System.out.println(method);
    }
}
