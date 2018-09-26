package com.delicloud.platform.cloud.storage.server.mq;

import com.delicloud.platform.cloud.storage.server.bo.SliceInfo;
import com.delicloud.platform.cloud.storage.server.service.FileService;
import com.delicloud.platform.common.lang.util.JsonUtil;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqFileService {
    @Autowired
    MqProcessor mqProcessor;

    @Autowired
    FileService fileService;

    public void fileMergeOutput(SliceInfo sliceInfo) {
        String payload = JsonUtil.getJsonFromObject(sliceInfo);
        mqProcessor.fileMergeOutput().send(MessageBuilder.withPayload(payload).build());
    }

    @StreamListener(MqProcessor.FILE_MERGE_INPUT)
    public void fileMergeInput(Message<String> message) {
        if (null == message.getPayload()) {
            return;
        }
        String payload = message.getPayload();
        SliceInfo sliceInfo = JsonUtil.getObjectFromJson(payload, SliceInfo.class);
        fileService.merge(sliceInfo);
    }
}
