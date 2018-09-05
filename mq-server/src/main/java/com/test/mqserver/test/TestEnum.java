package com.test.mqserver.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TestEnum {
    CloudAppOpenAction(2, "^(http|https)://delicloud\\.com/d/(\\w+)/appcall\\?to=(\\w+)&data=(.+)$"),
    DeviceLoginAction(4, "^(http|https)://qr\\.delicloud\\.com\\?(?=.*from=device)(?=.*action=login)(?=.*product=(\\w+))(?=.*device=(\\w+))(?=.*conntype=(\\w+)).*$"),
    FindUserInfoAction(5, "^(http|https)://delicloud\\.cn/barcode\\?(?=.*type=user)(?=.*user_id=(\\w+)).*$"),
    InitDeviceAction(6, "^(http|https)://qr\\.delicloud\\.com\\?(?=.*from=device)(?=.*action=activate)(?=.*product=(\\w+))(?=.*device=(\\w+))(?=.*conntype=(\\w+)).*$"),
    WebQrCodeAction(7, "^(http|https)://www\\.delidloud\\.cn\\?(?=.*from=barcode)(?=.*action=login)(?=.*url=([\\w:/.]+))(?=.*data=([\\w-]+)).*$"),
    ;

    int level;
    String regex;

    TestEnum(int level, String regex) {
        this.level = level;
        this.regex = regex;
    }

    public int getLevel() {
        return level;
    }

    public String getRegex() {
        return regex;
    }

    public static TestEnum getByUrl(String url) {
        for (TestEnum testEnum : values()) {
            String regex = testEnum.getRegex();
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(url); // 获取 matcher 对象
            if (m.find()) {
                return testEnum;
            }
        }
        return null;
    }
}
