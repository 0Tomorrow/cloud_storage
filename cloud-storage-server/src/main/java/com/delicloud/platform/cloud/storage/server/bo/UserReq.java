package com.delicloud.platform.cloud.storage.server.bo;

import lombok.Data;

@Data
public class UserReq {
    private String account;
    private String password;

    public Boolean verify() {
        if (account == null || password == null) {
            return false;
        }
        if (!account.matches("1[356789]\\d{9}")) {
            return false;
        }
        if (!password.matches(".{6,20}")){
            return false;
        }
        return true;
    }
}
