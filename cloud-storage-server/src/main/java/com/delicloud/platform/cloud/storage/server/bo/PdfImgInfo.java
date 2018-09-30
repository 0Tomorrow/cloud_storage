package com.delicloud.platform.cloud.storage.server.bo;

import lombok.Data;

@Data
public class PdfImgInfo {
    private String path;
    private Integer page;
    private Integer width;
    private Integer height;
}
