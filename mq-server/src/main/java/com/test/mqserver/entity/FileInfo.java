package com.test.mqserver.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "fileinfos")
@Data
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fileId;
    private String fileName;
    private int indexId;
    private Long userAccount;
    private Long time;
    private Long size;
    private String type;
    private int page;

    public FileInfo() {

    }

    public FileInfo(String fileName, int indexId, Long userAccount) {
        this.fileName = fileName;
        this.indexId = indexId;
        this.userAccount = userAccount;
    }
}
