package com.test.mqserver.service;

import com.test.mqserver.bo.FileBo;
import com.test.mqserver.config.ErrorCode;
import com.test.mqserver.config.PathConfig;
import com.test.mqserver.config.SpringContextProvider;
import com.test.mqserver.entity.FileInfo;
import com.test.mqserver.entity.IndexInfo;
import com.test.mqserver.mq.Sender;
import com.test.mqserver.repository.FileRepos;
import com.test.mqserver.repository.IndexRepos;
import com.test.mqserver.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FolderService {

    @Autowired
    Sender sender;

    @Autowired
    IndexRepos indexRepos;

    @Autowired
    FileRepos fileRepos;

    @Autowired
    PathConfig pathConfig;

    public void mqDeleteFolder(Long account, String relativePath) {
        FileBo fileBo = new FileBo(account, relativePath);
        sender.deleteFolder(fileBo);
    }

    public void updateFolder(Long account, String relativePath) {
        String path = pathConfig.getPath(account, relativePath);
        String filePath = pathConfig.getPath() + path;
        File targetFile = new File(filePath);
        System.out.println(filePath);
        if(targetFile.exists()){
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        String regex = "^(.*/)([^/]+)/$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(path); // 获取 matcher 对象
        if (!m.find()) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        String prevPath = m.group(1);
        String folderName = m.group(2);
        File prevFile = new File(pathConfig.getPath() + prevPath);
        if (!prevFile.exists()) {
            throw SpringContextProvider.createPlatformException(ErrorCode.PrevFolderDoesNotExist);
        }
        if (!targetFile.mkdirs()) {
            throw SpringContextProvider.createPlatformException(ErrorCode.CreateFolderException);
        }
        IndexInfo indexInfo = indexRepos.findByIndexPath(prevPath);
        if (indexInfo == null) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        int prevId = indexInfo.getIndexId();
        indexRepos.save(new IndexInfo(folderName, prevId, path, account));
    }

    public void deleteFolder(String path) {
        String filePath = pathConfig.getPath() + path;
        delAllFile(path);
        indexRepos.deleteAllByIndexPath(path);
        FileUtil.deleteFolder(filePath);
    }

    private void delAllFile(String path) {
        String filePath = pathConfig.getPath() + path;
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
                FileUtil.deleteFile(filePath + "/" + fileName);
            }
        }
        List<IndexInfo> indexList = indexRepos.findAllByPrevId(indexId);
        if (indexList != null) {
            for (IndexInfo folder : indexList) {
                deleteFolder(filePath + "/" + folder.getIndexName());
            }
        }
    }

    public boolean isExit(String folderPath) {
        File file = new File(folderPath);
        return file.exists() && file.isDirectory();
    }
}
