package com.test.mqserver.bo;

import lombok.Data;

@Data
public class FileResp {
    private String fileName;

    public FileResp(String fileName) {
        this.fileName = fileName;
    }
}
