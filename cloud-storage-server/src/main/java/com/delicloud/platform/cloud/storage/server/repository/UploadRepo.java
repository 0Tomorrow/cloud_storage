package com.delicloud.platform.cloud.storage.server.repository;

import com.delicloud.platform.cloud.storage.server.entity.TUploadInfo;
import com.delicloud.platform.common.data.repository.MyRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UploadRepo extends MyRepository<TUploadInfo, Long>{
    TUploadInfo findFirstByMergeCode(String mergeCode);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from t_upload_info where t_file_info_id = ?1")
    void deleteAllByTFileInfo(Long tFileInfoId);
}
