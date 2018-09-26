package com.delicloud.platform.cloud.storage.server.repository;

import com.delicloud.platform.cloud.storage.server.entity.TUploadInfo;
import com.delicloud.platform.common.data.repository.MyRepository;

public interface UploadRepo extends MyRepository<TUploadInfo, Long>{
    TUploadInfo findFirstByMergeCode(String mergeCode);
}
