package com.test.mqserver.bo;

import com.test.mqserver.util.FileUtil;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class FileBo implements Serializable {
    private String fileName;
    private byte[] file;
    private Long account;
    private String relativePath;

    public FileBo(byte[] file, String fileName, Long account, String relativePath) {
        this.fileName = fileName;
        this.file = file;
        this.account = account;
        this.relativePath = relativePath;
    }

    public FileBo(MultipartFile file, Long account, String relativePath) {
        try {
            this.file = FileUtil.fileToStringBuffer(file, file.getSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.fileName = file.getOriginalFilename();
        this.account = account;
        this.relativePath = relativePath;
    }

    public FileBo(Long account, String relativePath) {
        this.account = account;
        this.relativePath = relativePath;
    }

    public FileBo(String fileName, Long account, String relativePath) {
        this.fileName = fileName;
        this.account = account;
        this.relativePath = relativePath;
    }

    public FileBo() {
    }

    public String toString() {
        return "{fileName : " + fileName + ", account : " + account + ", relativePath : " + relativePath + "}";
    }
}
