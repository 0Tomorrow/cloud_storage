package com.test.mqserver.mq;

import com.test.mqserver.bo.FileBo;
import com.test.mqserver.bo.FileSliceBo;
import com.test.mqserver.bo.FileSliceInfo;
import com.test.mqserver.cache.FileSliceCache;
import com.test.mqserver.config.ErrorCode;
import com.test.mqserver.config.PathConfig;
import com.test.mqserver.config.SpringContextProvider;
import com.test.mqserver.service.FileService;
import com.test.mqserver.service.FolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Receiver {
    @Autowired
    FileService fileService;

    @Autowired
    FolderService folderService;

    @Autowired
    FileSliceCache fileSliceCache;

    @Autowired
    PathConfig pathConfig;

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("handShake"))
    public void handShake(FileSliceBo fileSliceBo) {
        FileSliceInfo fileSliceInfo = fileSliceBo.getFileSliceInfo();
        System.out.println(fileSliceInfo);
        fileSliceCache.putFileSliceInfo(fileSliceInfo.getIdCode(), fileSliceInfo);
        if (fileSliceCache.getFileSliceInfo(fileSliceInfo.getIdCode()) == null) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        System.out.println(fileSliceCache.getFileSliceInfo(fileSliceInfo.getIdCode()));
        fileService.createTempFile(fileSliceInfo);
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
    @RabbitListener(queuesToDeclare = @Queue("deleteFile"))
    public void deleteFile(FileBo fileBo) {
        String fileName = fileBo.getFileName();
        Long account = fileBo.getAccount();
        String relativePath = fileBo.getRelativePath();
        log.debug("deleteFile fileBo : {}", fileBo);
        fileService.deleteFile(fileName, account, relativePath);
    }

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("deleteFolder"))
    public void deleteFolder(FileBo fileBo) {
        Long account = fileBo.getAccount();
        String relativePath = fileBo.getRelativePath();
        log.debug("deleteFolder fileBo : {}", fileBo);
        String path = pathConfig.getPath(account, relativePath);
        folderService.deleteFolder(path);
    }
}
