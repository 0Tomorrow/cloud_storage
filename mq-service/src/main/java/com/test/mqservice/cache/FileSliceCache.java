package com.test.mqservice.cache;

import com.test.mqcore.bo.FileSliceInfo;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
