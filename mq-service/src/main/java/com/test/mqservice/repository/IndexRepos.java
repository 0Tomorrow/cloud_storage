package com.test.mqservice.repository;

import com.test.mqcore.entity.IndexInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IndexRepos extends JpaRepository<IndexInfo, Integer>, JpaSpecificationExecutor<Integer> {

    IndexInfo findByIndexPath(String path);

    List<IndexInfo> findAllByPrevId(int prevId);
}
