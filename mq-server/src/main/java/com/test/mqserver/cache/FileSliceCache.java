package com.test.mqserver.cache;

import com.test.mqserver.bo.FileSliceInfo;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class FileSliceCache {
    @CachePut(value = "fileSliceInfo", key = "#key.toString()", condition = "#key != null")
    public FileSliceInfo putFileSliceInfo(String key, FileSliceInfo fileSliceInfo) {
        return fileSliceInfo;
    }

    @Cacheable(value = "fileSliceInfo", key = "#key.toString()", condition = "#key != null")
    public FileSliceInfo getFileSliceInfo(String key) {
        return null;
    }
}
