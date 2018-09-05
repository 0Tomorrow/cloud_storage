package com.test.mqserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "file")
public class PathConfig {
    private String path;

    public String getPath(Long account, String relativePath) {
        if (relativePath == null || relativePath.equals("")) {
            return account + "/";
        }
        if (relativePath.charAt(0) != '/') {
            relativePath = "/" + relativePath;
        }
        if (relativePath.charAt(relativePath.length() - 1) != '/') {
            relativePath = relativePath + "/";
        }
        return account + relativePath;
    }
}
