package com.test.mqserver.repository;

import com.test.mqserver.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FileRepos extends JpaRepository<FileInfo, Integer>, JpaSpecificationExecutor<Integer> {
    List<FileInfo> findAllByIndexId(int indexId);

    void deleteAllByFileNameAndIndexId(String fileName, int indexId);
}
