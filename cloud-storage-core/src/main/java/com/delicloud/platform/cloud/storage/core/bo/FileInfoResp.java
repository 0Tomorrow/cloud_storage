package com.delicloud.platform.cloud.storage.core.bo;

import lombok.Data;

@Data
public class FileInfoResp {
    private String id;
    private String fileName;
    private String type;
    private Long updateTime;
    private Long fileSize;
    private String icon;
}
