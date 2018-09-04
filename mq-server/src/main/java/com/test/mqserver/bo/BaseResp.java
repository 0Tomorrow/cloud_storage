package com.test.mqserver.bo;

import lombok.Data;

@Data
public class BaseResp<T> {
    private int code = 0;
    private String msg;
    private T data;

    public BaseResp(T data) {
        this.data = data;
    }
}
