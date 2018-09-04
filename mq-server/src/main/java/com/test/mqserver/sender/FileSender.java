package com.test.mqserver.sender;

import com.test.mqserver.bo.FileBo;
import com.test.mqserver.bo.FileSliceBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void handShake(FileSliceBo fileSliceBo) {
        this.rabbitTemplate.convertAndSend("handShake", fileSliceBo);
    }

    public void uploadSliceFile(FileSliceBo fileSliceBo) {
        this.rabbitTemplate.convertAndSend("uploadSliceFile", fileSliceBo);
    }

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
