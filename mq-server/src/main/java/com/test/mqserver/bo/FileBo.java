package com.test.mqserver.bo;

import lombok.Data;
import java.io.Serializable;

@Data
public class FileBo implements Serializable {

    private String fileName;
    private Long account;
    private String relativePath;

    public FileBo(Long account, String relativePath) {
        this.account = account;
        this.relativePath = relativePath;
    }

    public FileBo(String fileName, Long account, String relativePath) {
        this.fileName = fileName;
        this.account = account;
        this.relativePath = relativePath;
    }

    public String toString() {
        return "{fileName : " + fileName + ", account : " + account + ", relativePath : " + relativePath + "}";
    }
}
