package com.test.mqcore.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "indexinfos")
@Data
public class IndexInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int indexId;
    private String indexName;
    private int prevId;
    private String indexPath;
    private Long userAccount;

    public IndexInfo() {

    }

    public IndexInfo(String indexName, int prevId, String indexPath, Long userAccount) {
        this.indexName = indexName;
        this.prevId = prevId;
        this.indexPath = indexPath;
        this.userAccount = userAccount;
    }
}
