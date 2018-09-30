package com.delicloud.platform.cloud.storage.server.config;

import com.delicloud.platform.cloud.storage.server.bo.PdfImgInfo;
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

    public List<PdfImgInfo> pdfPreview(String path) {
        String fileMd5 = FileUtil.getFileMd5(path);
        if (null == fileMd5) {
            throw new PlatformException("创建文件md5失败");
        }
        String imgPath = pdfImgAbsolutePath + fileMd5 + "/";
        String relativePath = pdfImgRelativePath + fileMd5 + "/";
        return PdfUtil.pdfToImageList(path, imgPath, relativePath);
    }
}
