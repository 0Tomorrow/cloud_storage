package com.test.mqservice.receiver;


import com.test.mqcore.bo.FileBo;
import com.test.mqcore.bo.BaseResp;
import com.test.mqcore.bo.FileResp;
import com.test.mqservice.service.FileService;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
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
        FileResp fileResp = fileService.uploadFile(file, fileName, account, relativePath);
    }

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("createFolder"))
    public void createFolder(FileBo fileBo) {
        Long account = fileBo.getAccount();
        String relativePath = fileBo.getRelativePath();
        FileResp fileResp = fileService.updataFolder(account, relativePath);
    }

}
