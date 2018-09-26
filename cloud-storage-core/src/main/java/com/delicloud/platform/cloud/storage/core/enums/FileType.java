package com.delicloud.platform.cloud.storage.core.enums;

public enum FileType {
    TXT(1, "txt"),
    PDF(2, "pdf"),
    EXE(3, "exe");

    private Integer code;
    private String type;
    FileType(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

}
