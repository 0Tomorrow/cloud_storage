package com.test.mqserver.service;

import com.test.mqserver.repository.FileRepos;
import com.test.mqserver.repository.IndexRepos;
import com.test.mqcore.config.ErrorCode;
import com.test.mqcore.config.PathConfig;
import com.test.mqcore.config.SpringContextProvider;
import com.test.mqcore.entity.FileInfo;
import com.test.mqcore.entity.IndexInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {
    @Autowired
    FileRepos fileRepos;

    @Autowired
    IndexRepos indexRepos;

    public List<IndexInfo> showFolder(Long account, String relativePath) {
        String filePath = PathConfig.getPath(account, relativePath);
        IndexInfo indexInfo = indexRepos.findByIndexPath(filePath);
        if (indexInfo == null) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        int indexId = indexInfo.getIndexId();
        List<IndexInfo> indexList = indexRepos.findAllByPrevId(indexId);
        return indexList;
    }

    public List<FileInfo> showFile(Long account, String relativePath) {
        String filePath = PathConfig.getPath(account, relativePath);
        IndexInfo indexInfo = indexRepos.findByIndexPath(filePath);
        if (indexInfo == null) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        int indexId = indexInfo.getIndexId();
        List<FileInfo> fileList = fileRepos.findAllByIndexId(indexId);
        return fileList;
    }
}
