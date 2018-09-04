package com.test.mqserver.repository;

import com.test.mqserver.entity.IndexInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IndexRepos extends JpaRepository<IndexInfo, Integer>, JpaSpecificationExecutor<Integer> {

    IndexInfo findByIndexPath(String path);

    List<IndexInfo> findAllByPrevId(int prevId);

    @Modifying
    @Transactional
    void deleteAllByIndexPath(String indexPath);
}
