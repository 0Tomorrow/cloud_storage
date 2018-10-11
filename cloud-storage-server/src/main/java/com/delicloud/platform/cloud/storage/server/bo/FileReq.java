package com.delicloud.platform.cloud.storage.server.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class FileReq {
    private String id;
    private String newName;
}
