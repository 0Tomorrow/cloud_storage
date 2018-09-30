package com.delicloud.platform.cloud.storage.core.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum FileType {
    TXT(1, "txt"),
    PDF(2, "pdf"),
    EXE(3, "exe"),
    GIF(4, "gif");

    private Integer code;
    private String type;
    FileType(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
