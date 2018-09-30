package com.delicloud.platform.cloud.storage.server.bo;

import lombok.Data;

@Data
public class PdfImgInfo {
    private String pdfPath;
    private Integer pdfIndex;
    private Integer pdfWidth;
    private Integer pdfHeight;
}
