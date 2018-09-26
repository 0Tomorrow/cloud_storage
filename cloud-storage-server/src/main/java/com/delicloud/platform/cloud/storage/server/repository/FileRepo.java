package com.delicloud.platform.cloud.storage.server.repository;

import com.delicloud.platform.cloud.storage.server.entity.TFileInfo;
import com.delicloud.platform.common.data.repository.MyRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface FileRepo extends MyRepository<TFileInfo, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM t_file_info t1 " +
            "JOIN t_index_info t2 ON t1.index_info_id = t2.id " +
            "WHERE t2.path = ?2 AND t1.update_by = ?1")
    List<TFileInfo> findAllByUpdateByAndPath(Long account, String path);

    @Transactional
    @Query(nativeQuery = true, value = "DELETE * FROM t_file_info t1 " +
            "JOIN t_index_info t2 on t1.index_info_id = t2.id " +
            "WHERE t1.update_by = ?1 and t2.path = ?2 and t1.file_name = ?3")
    void deleteAllByUpdateByAndPathAndFileName(Long account, String path, String fileName);

    @Transactional
    @Query(nativeQuery = true, value = "DELETE * FROM t_file_info t1 " +
            "JOIN t_index_info t2 on t1.index_info_id = t2.id " +
            "WHERE t1.update_by = ?1 and t2.path = ?2")
    void deleteAllByUpdateByAndPath(Long account, String path);

    List<TFileInfo> findByUpdateBy(Long updateBy, Pageable page);
}
