package com.test.mqserver.sender;

import com.test.mqcore.bo.FileBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class FileSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void uploadFile(FileBo fileBo) {
        log.debug("uploadFile fileBo : {}", fileBo);
        this.rabbitTemplate.convertAndSend("uploadFile", fileBo);
    }

    public void updataFolder(FileBo fileBo) {
        log.debug("updataFolder fileBo : {}", fileBo);
        this.rabbitTemplate.convertAndSend("createFolder", fileBo);
    }

    public void deleteFile(FileBo fileBo) {
        log.debug("deleteFile fileBo : {}", fileBo);
        this.rabbitTemplate.convertAndSend("deleteFile", fileBo);
    }

    public void deleteFolder(FileBo fileBo) {
        log.debug("deleteFolder fileBo : {}", fileBo);
        this.rabbitTemplate.convertAndSend("deleteFolder", fileBo);
    }
}
