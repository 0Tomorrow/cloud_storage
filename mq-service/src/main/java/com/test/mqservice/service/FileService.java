package com.test.mqservice.service;

import com.test.mqcore.entity.FileInfo;
import com.test.mqcore.entity.IndexInfo;
import com.test.mqcore.config.ErrorCode;
import com.test.mqcore.config.PathConfig;
import com.test.mqcore.config.SpringContextProvider;
import com.test.mqservice.repository.FileRepos;
import com.test.mqservice.repository.IndexRepos;
import com.test.mqservice.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class FileService {

    @Autowired
    FileRepos fileRepos;

    @Autowired
    IndexRepos indexRepos;

    public void uploadFile(StringBuffer file, String fileName, Long account, String relativePath) {
        if (fileName == null || fileName.equals("")) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        String filePath = PathConfig.getPath(account, relativePath);
        IndexInfo indexInfo = indexRepos.findByIndexPath(filePath);
        if (indexInfo == null) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        try {
            fileName = FileUtil.uploadFile(file, filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int indexId = indexInfo.getIndexId();
        fileRepos.save(new FileInfo(fileName, indexId, account));
    }

    public void updataFolder(Long account, String relativePath) {
        String path = PathConfig.getPath(account, relativePath);
        File targetFile = new File(path);
        if(targetFile.exists()){
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        String regex = "^(.*)/([^/]+)$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(path); // 获取 matcher 对象
        if (!m.find()) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        String prevPath = m.group(1);
        String folderName = m.group(2);
        File prevFile = new File(prevPath);
        if (!prevFile.exists()) {
            throw SpringContextProvider.createPlatformException(ErrorCode.PrevFolderDoesNotExist);
        }
        if (!targetFile.mkdirs()) {
            throw SpringContextProvider.createPlatformException(ErrorCode.CreateFolderException);
        }
        if (indexRepos.findByIndexPath(prevPath) == null) {
            indexRepos.save(new IndexInfo(account.toString(), 0, prevPath, account));
        }
        IndexInfo indexInfo = indexRepos.findByIndexPath(prevPath);
        if (indexInfo == null) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        int prevId = indexInfo.getIndexId();
        indexRepos.save(new IndexInfo(folderName, prevId, path, account));
    }

    public void deleteFile(String fileName, Long account, String relativePath) {
        if (fileName == null || fileName.equals("")) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        String path = PathConfig.getPath(account, relativePath);
        IndexInfo indexInfo = indexRepos.findByIndexPath(path);
        if (indexInfo == null) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        try {
            FileUtil.deleteFile(path + "/" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int indexId = indexInfo.getIndexId();
        fileRepos.deleteAllByFileNameAndIndexId(fileName, indexId);
    }

    public void deleteFolder(Long account, String relativePath) {
        String path = PathConfig.getPath(account, relativePath);
        delAllFile(account, relativePath);
        indexRepos.deleteAllByIndexPath(path);
        FileUtil.deleteFolder(path);
    }

    public void delAllFile(Long account, String relativePath) {
        String path = PathConfig.getPath(account, relativePath);
        IndexInfo indexInfo = indexRepos.findByIndexPath(path);
        if (indexInfo == null) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        int indexId = indexInfo.getIndexId();
        List<FileInfo> fileList = fileRepos.findAllByIndexId(indexId);
        if (fileList != null) {
            for (FileInfo file : fileList) {
                String fileName = file.getFileName();
                fileRepos.deleteAllByFileNameAndIndexId(fileName, indexId);
                FileUtil.deleteFile(path + "/" + fileName);
            }
        }
        List<IndexInfo> indexList = indexRepos.findAllByPrevId(indexId);
        if (indexList != null) {
            for (IndexInfo folder : indexList) {
                deleteFolder(account, relativePath + "/" + folder.getIndexName());
            }
        }
    }
}
