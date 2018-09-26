package com.delicloud.platform.cloud.storage.core.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileInfo implements Serializable {
    private String fileName;

    private String path;

    private String type;

    private Long fileSize;

    private Integer fileState;

    private Long sliceSize;

    private String mergeCode;

    private Integer sliceCount;

    private Integer uploadCount;

    private Long updateBy;

    private Long updateTime;

    private String icon;

}
