package com.test.mqserver.sender;

import com.test.mqcore.bo.FileBo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void uploadFile(MultipartFile file, Long account, String relativePath) {
        FileBo fileBo = new FileBo(file, account, relativePath);
//        String s = JSON.toJSONString(fileBo);
        this.rabbitTemplate.convertAndSend("uploadFile", fileBo);
    }

    public void updataFolder(Long account, String relativePath) {
        FileBo fileBo = new FileBo(account, relativePath);
//        String s = JSON.toJSONString(fileBo);
        this.rabbitTemplate.convertAndSend("createFolder", fileBo);
    }
}
