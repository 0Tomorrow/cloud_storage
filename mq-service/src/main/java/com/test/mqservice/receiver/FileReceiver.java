package com.test.mqservice.receiver;

import com.test.mqcore.bo.FileBo;
import com.test.mqservice.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileReceiver {
    @Autowired
    FileService fileService;

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("uploadFile"))
    public void uploadFile(FileBo fileBo) {
        String fileName = fileBo.getFileName();
        StringBuffer file = fileBo.getFile();
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
        fileService.deleteFile(fileName, account, relativePath);
    }

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("deleteFolder"))
    public void deleteFolder(FileBo fileBo) {
        Long account = fileBo.getAccount();
        String relativePath = fileBo.getRelativePath();
        log.debug("deleteFolder fileBo : {}", fileBo);
        fileService.deleteFolder(account, relativePath);
    }
}
