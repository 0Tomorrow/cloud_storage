package com.delicloud.platform.cloud.storage.server.bo;

import lombok.Data;

@Data
public class SliceInfo {
    private String mergeCode;
    private byte[] file;
    private Integer index;
}
