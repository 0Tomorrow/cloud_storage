package com.test.mqservice.repository;

import com.test.mqcore.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FileRepos extends JpaRepository<FileInfo, Integer>, JpaSpecificationExecutor<Integer> {
    List<FileInfo> findAllByIndexId(int indexId);
}
