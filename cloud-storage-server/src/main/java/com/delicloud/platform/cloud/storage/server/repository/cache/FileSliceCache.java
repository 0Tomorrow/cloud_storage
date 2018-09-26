package com.delicloud.platform.cloud.storage.server.repository.cache;

import com.delicloud.platform.cloud.storage.server.bo.SliceInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FileSliceCache {
    @CachePut(value = "SliceInfo", key = "#key.toString()", condition = "#key != null")
    public SliceInfo putSliceInfo(String key, SliceInfo SliceInfo) {
        return SliceInfo;
    }

    @Cacheable(value = "SliceInfo", key = "#key.toString()", condition = "#key != null")
    public SliceInfo getSliceInfo(String key) {
        return null;
    }

    @CacheEvict(value = "SliceInfo", key = "#mergeCode.toString()", condition = "#key != null")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void evictByMergeCode(String mergeCode) {
        return;
    }

}
