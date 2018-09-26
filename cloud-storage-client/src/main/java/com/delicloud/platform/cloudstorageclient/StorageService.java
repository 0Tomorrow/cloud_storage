package com.delicloud.platform.cloudstorageclient;

import com.delicloud.platform.cloud.storage.core.bo.FileInfo;
import com.delicloud.platform.cloud.storage.core.bo.FileInfoResp;
import com.delicloud.platform.cloud.storage.core.bo.IndexInfo;
import com.delicloud.platform.cloud.storage.core.bo.IndexInfoResp;
import com.delicloud.platform.cloudstorageclient.impl.StorageClient;
import com.delicloud.platform.common.lang.bo.RespBase;
import com.delicloud.platform.common.lang.exception.PlatformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service("RemoteStorageService")
public class StorageService {

    @Autowired
    StorageClient storageClient;

    List<FileInfoResp> showFile(Long account, String path) {
        RespBase<List<FileInfoResp>> respBase = storageClient.showFile(account, path);
        try {
            return respBase.validateAndReturn();
        } catch (PlatformException ex) {
            return null;
        }
    }

    String handShake(FileInfo fileInfo) {
        RespBase<String> respBase = storageClient.handShake(fileInfo);
        try {
            return respBase.validateAndReturn();
        } catch (PlatformException ex) {
            return null;
        }
    }

    Void sliceUpload(MultipartFile file, Integer index, String mergeCode) {
        return storageClient.sliceUpload(file, index, mergeCode).validateAndReturn();
    }

    Void deleteFile(String path, String fileName, Long account) {
        return storageClient.deleteFile(path, fileName, account).validateAndReturn();
    }

    List<IndexInfoResp> showIndex(IndexInfo indexInfo) {
        RespBase<List<IndexInfoResp>> respBase = storageClient.showIndex(indexInfo);
        try {
            return respBase.validateAndReturn();
        } catch (PlatformException e) {
            return null;
        }
    }

    Void createIndex(IndexInfo indexInfo) {
        return storageClient.createIndex(indexInfo).validateAndReturn();
    }

    Void deleteIndex(IndexInfo indexInfo, boolean sure) {
        return storageClient.deleteIndex(indexInfo, sure).validateAndReturn();
    }
}
