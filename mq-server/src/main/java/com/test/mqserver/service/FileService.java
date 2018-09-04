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
import com.test.mqserver.sender.FileSender;
import com.test.mqserver.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileService {

    @Autowired
    FileSender fileSender;

    @Autowired
    UserRepos userRepos;

    @Autowired
    FileRepos fileRepos;

    @Autowired
    IndexRepos indexRepos;

    public void handShake(FileSliceInfo fileSliceInfo) {
        Long account = fileSliceInfo.getAccount();
        File file = new File(PathConfig.getPath(account, ""));
        if (!file.exists() || !file.isDirectory()) {
            if (userRepos.findFirstByAccount(account) != null) {
                file.mkdirs();
            }
        }
        FileSliceBo fileSliceBo = new FileSliceBo();
        fileSliceBo.setFileSliceInfo(fileSliceInfo);
        fileSender.handShake(fileSliceBo);
    }

    public void uploadSliceFile(MultipartFile file, int index, String idCode) {
        FileSliceBo fileSliceBo = new FileSliceBo(file, index, idCode);
        fileSender.uploadSliceFile(fileSliceBo);
    }

    public void uploadFile(MultipartFile file, Long account, String relativePath) {
        FileBo fileBo = new FileBo(file, account, relativePath);
        fileSender.uploadFile(fileBo);
    }

    public void deleteFile(String fileName, Long account, String relativePath) {
        FileBo fileBo = new FileBo(fileName, account, relativePath);
        fileSender.deleteFile(fileBo);
    }

    public void deleteFolder(Long account, String relativePath) {
        FileBo fileBo = new FileBo(account, relativePath);
        fileSender.deleteFolder(fileBo);
    }

    public FileSliceInfo uploadSliceFile(byte[] file, int index, FileSliceInfo fileSliceInfo) {
        FileSliceInfo fileSlice = FileUtil.merge(file, index, fileSliceInfo);
        if (!fileSlice.isFinish()) {
            return fileSlice;
        }
        String fileName = fileSlice.getFileName();
        Long account = fileSlice.getAccount();
        String relativePath = fileSlice.getRelativePath();
        String filePath = PathConfig.getPath(account, relativePath);
        IndexInfo indexInfo = indexRepos.findByIndexPath(filePath);
        if (indexInfo == null) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        int indexId = indexInfo.getIndexId();
        fileRepos.save(new FileInfo(fileName, indexId, account));
        return fileSlice;
    }

    public void creatTempFile(FileSliceInfo fileSliceInfo){
        FileUtil.createTempFile(fileSliceInfo);
    }

    public void uploadFile(byte[] file, String fileName, Long account, String relativePath) {
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

    public void deleteFile(String fileName, String path) {
        if (fileName == null || fileName.equals("")) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
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

    public void deleteFolder(String path) {
        delAllFile(path);
        indexRepos.deleteAllByIndexPath(path);
        FileUtil.deleteFolder(path);
    }

    public void delAllFile(String path) {
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
                deleteFolder(path + "/" + folder.getIndexName());
            }
        }
    }}
