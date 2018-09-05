package com.test.mqserver.service;

import com.test.mqserver.bo.FileBo;
import com.test.mqserver.bo.FileSliceBo;
import com.test.mqserver.bo.FileSliceInfo;
import com.test.mqserver.config.ErrorCode;
import com.test.mqserver.config.PathConfig;
import com.test.mqserver.config.SpringContextProvider;
import com.test.mqserver.entity.FileInfo;
import com.test.mqserver.entity.IndexInfo;
import com.test.mqserver.repository.FileRepos;
import com.test.mqserver.repository.IndexRepos;
import com.test.mqserver.repository.UserRepos;
import com.test.mqserver.mq.Sender;
import com.test.mqserver.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Autowired
    Sender sender;

    @Autowired
    UserRepos userRepos;

    @Autowired
    FileRepos fileRepos;

    @Autowired
    IndexRepos indexRepos;

    @Autowired
    PathConfig pathConfig;

    public void handShake(FileSliceInfo fileSliceInfo) {
        Long account = fileSliceInfo.getAccount();
        String relativePath = fileSliceInfo.getRelativePath();
        String idCode = fileSliceInfo.getIdCode();
        String fileName = fileSliceInfo.getFileName();
        String path = pathConfig.getPath(account, relativePath);
        String filePath = pathConfig.getPath() + path;
        String tempPath = filePath + idCode + ".tmp";
        filePath = filePath + fileName;
        fileSliceInfo.setFilePath(filePath);
        fileSliceInfo.setTempPath(tempPath);
        FileSliceBo fileSliceBo = new FileSliceBo();
        fileSliceBo.setFileSliceInfo(fileSliceInfo);
        sender.handShake(fileSliceBo);
    }

    public void mqUploadSliceFile(MultipartFile file, int index, String idCode) {
        FileSliceBo fileSliceBo = new FileSliceBo(file, index, idCode);
        sender.uploadSliceFile(fileSliceBo);
    }

    public void mqDeleteFile(String fileName, Long account, String relativePath) {
        FileBo fileBo = new FileBo(fileName, account, relativePath);
        sender.deleteFile(fileBo);
    }

    public FileSliceInfo uploadSliceFile(byte[] file, int index, FileSliceInfo fileSliceInfo) {
        FileSliceInfo fileSlice = FileUtil.merge(file, index, fileSliceInfo);
        if (!fileSlice.isFinish()) {
            return fileSlice;
        }
        String fileName = fileSlice.getFileName();
        Long account = fileSlice.getAccount();
        String relativePath = fileSlice.getRelativePath();
        String path = pathConfig.getPath(account, relativePath);
        String filePath = pathConfig.getPath() + path;
        IndexInfo indexInfo = indexRepos.findByIndexPath(path);
        if (indexInfo == null) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        int indexId = indexInfo.getIndexId();
        fileRepos.save(new FileInfo(fileName, indexId, account));
        return fileSlice;
    }

    public void createTempFile(FileSliceInfo fileSliceInfo) {
        FileUtil.createTempFile(fileSliceInfo);
    }

    public void deleteFile(String fileName, Long account, String relativePath) {
        String path = pathConfig.getPath(account, relativePath);
        String filePath = pathConfig.getPath() + path;
        if (fileName == null || fileName.equals("")) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        IndexInfo indexInfo = indexRepos.findByIndexPath(path);
        if (indexInfo == null) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        try {
            FileUtil.deleteFile(filePath + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int indexId = indexInfo.getIndexId();
        fileRepos.deleteAllByFileNameAndIndexId(fileName, indexId);
    }
}
