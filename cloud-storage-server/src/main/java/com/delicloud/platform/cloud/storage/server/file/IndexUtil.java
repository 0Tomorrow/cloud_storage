package com.delicloud.platform.cloud.storage.server.file;

import com.delicloud.platform.common.lang.exception.PlatformException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class IndexUtil {
    public static void createIndex(String indexPath) {
        File file = new File(indexPath);
        if (!file.mkdir()) {
            throw new PlatformException("文件夹创建失败: " + indexPath);
        }
    }

    public static void deleteIndex(String indexPath) {
        File file = new File(indexPath);
        if (!file.isDirectory()) {
            throw new PlatformException("该路径不是文件夹: " + indexPath);
        }
        if (!file.delete()) {
            throw new PlatformException("文件夹删除失败: " + indexPath);
        }
    }
}
