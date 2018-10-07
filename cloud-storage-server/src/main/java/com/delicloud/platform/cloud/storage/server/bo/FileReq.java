package com.delicloud.platform.cloud.storage.server.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class FileReq {
    private Long account;
    private String path;
    private String fileName;
}
