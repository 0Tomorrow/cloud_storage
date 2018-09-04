package com.test.mqserver.receiver;

import com.test.mqserver.bo.FileBo;
import com.test.mqserver.bo.FileSliceBo;
import com.test.mqserver.bo.FileSliceInfo;
import com.test.mqserver.cache.FileSliceCache;
import com.test.mqserver.config.PathConfig;
import com.test.mqserver.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileReceiver {
    @Autowired
    FileService fileService;

    @Autowired
    FileSliceCache fileSliceCache;

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("handShake"))
    public void handShake(FileSliceBo fileSliceBo) {
        FileSliceInfo fileSliceInfo = fileSliceBo.getFileSliceInfo();
        fileSliceCache.putFileSliceInfo(fileSliceInfo.getIdCode(), fileSliceInfo);
        fileService.creatTempFile(fileSliceInfo);
    }

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("uploadSliceFile"))
    public void uploadSliceFile(FileSliceBo fileSliceBo) {
        byte[] file = fileSliceBo.getFile();
        int index = fileSliceBo.getIndex();
        String idCode = fileSliceBo.getIdCode();
        FileSliceInfo fileSliceInfo = fileSliceCache.getFileSliceInfo(idCode);
        fileSliceInfo = fileService.uploadSliceFile(file, index, fileSliceInfo);
        fileSliceCache.putFileSliceInfo(fileSliceInfo.getIdCode(), fileSliceInfo);
    }

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("uploadFile"))
    public void uploadFile(FileBo fileBo) {
        String fileName = fileBo.getFileName();
        byte[] file = fileBo.getFile();
        Long account = fileBo.getAccount();
        String relativePath = fileBo.getRelativePath();
        log.debug("uploadFile fileBo : {}", fileBo);
        fileService.uploadFile(file, fileName, account, relativePath);
    }

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("createFolder"))
    public void createFolder(FileBo fileBo) {
        Long account = fileBo.getAccount();
        String relativePath = fileBo.getRelativePath();
        log.debug("createFolder fileBo : {}", fileBo);
        fileService.updataFolder(account, relativePath);
    }

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("deleteFile"))
    public void deleteFile(FileBo fileBo) {
        String fileName = fileBo.getFileName();
        Long account = fileBo.getAccount();
        String relativePath = fileBo.getRelativePath();
        log.debug("deleteFile fileBo : {}", fileBo);
        String path = PathConfig.getPath(account, relativePath);
        fileService.deleteFile(fileName, path);
    }

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("deleteFolder"))
    public void deleteFolder(FileBo fileBo) {
        Long account = fileBo.getAccount();
        String relativePath = fileBo.getRelativePath();
        log.debug("deleteFolder fileBo : {}", fileBo);
        String path = PathConfig.getPath(account, relativePath);
        fileService.deleteFolder(path);
    }
}
