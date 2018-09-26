package com.delicloud.platform.cloud.storage.core.bo;

import lombok.Data;

@Data
public class FileInfoResp {
    private String fileName;
    private Long updateTime;
    private Long size;
    private String icon;
}
