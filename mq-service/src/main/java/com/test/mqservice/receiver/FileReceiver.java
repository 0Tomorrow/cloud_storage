package com.test.mqservice.receiver;

import com.test.mqcore.bo.FileBo;
import com.test.mqcore.bo.FileSliceBo;
import com.test.mqcore.bo.FileSliceInfo;
import com.test.mqcore.config.PathConfig;
import com.test.mqservice.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.test.mqservice.cache.FileSliceCache;

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
