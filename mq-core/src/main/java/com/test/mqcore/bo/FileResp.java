package com.test.mqcore.bo;

import lombok.Data;

@Data
public class FileResp {
    private String fileName;

    public FileResp(String fileName) {
        this.fileName = fileName;
    }
}
