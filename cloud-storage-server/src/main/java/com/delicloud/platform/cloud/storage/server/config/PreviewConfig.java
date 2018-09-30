package com.delicloud.platform.cloud.storage.server.config;

import com.delicloud.platform.cloud.storage.server.bo.PdfImgInfo;
import com.delicloud.platform.cloud.storage.server.entity.TPdfInfo;
import com.delicloud.platform.cloud.storage.server.file.FileUtil;
import com.delicloud.platform.cloud.storage.server.util.PdfUtil;
import com.delicloud.platform.common.lang.exception.PlatformException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "preview")
public class PreviewConfig {
    private String pdfImgRelativePath;
    private String pdfImgAbsolutePath;

    public List<TPdfInfo> pdfPreview(String path, String pdfMd5) {
        String imgPath = pdfImgAbsolutePath + pdfMd5 + "/";
        String relativePath = pdfImgRelativePath + pdfMd5 + "/";
        List<TPdfInfo> list = PdfUtil.pdfToImageList(path, imgPath, relativePath);
        if (list == null) {
            return null;
        }
        for (TPdfInfo tPdfInfo : list) {
            tPdfInfo.setPdfMd5(pdfMd5);
        }
        return list;
    }
}
