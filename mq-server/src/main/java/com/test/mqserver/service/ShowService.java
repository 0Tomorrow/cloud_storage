package com.test.mqserver.service;

import com.test.mqserver.entity.UserInfo;
import com.test.mqserver.repository.FileRepos;
import com.test.mqserver.repository.IndexRepos;
import com.test.mqserver.config.ErrorCode;
import com.test.mqserver.config.PathConfig;
import com.test.mqserver.config.SpringContextProvider;
import com.test.mqserver.entity.FileInfo;
import com.test.mqserver.entity.IndexInfo;
import com.test.mqserver.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {
    @Autowired
    FileRepos fileRepos;

    @Autowired
    IndexRepos indexRepos;

    @Autowired
    UserRepos userRepos;

    @Autowired
    FolderService folderService;

    @Autowired
    PathConfig pathConfig;

    public List<IndexInfo> showFolder(Long account, String relativePath) {
        String path = pathConfig.getPath(account, relativePath);
        IndexInfo indexInfo = indexRepos.findByIndexPath(path);
        int indexId = indexInfo.getIndexId();
        return indexRepos.findAllByPrevId(indexId);
    }

    public List<FileInfo> showFile(Long account, String relativePath) {
        String path = pathConfig.getPath(account, relativePath);
        IndexInfo indexInfo = indexRepos.findByIndexPath(path);
        int indexId = indexInfo.getIndexId();
        return fileRepos.findAllByIndexId(indexId);
    }
}
