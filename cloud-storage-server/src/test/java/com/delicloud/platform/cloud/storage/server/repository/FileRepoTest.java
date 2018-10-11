package com.delicloud.platform.cloud.storage.server.repository;

import com.delicloud.platform.common.lang.mq.MQMessageSender;
import com.delicloud.platform.cloud.storage.server.Application;
import com.delicloud.platform.cloud.storage.server.entity.TUploadInfo;
import com.delicloud.platform.cloud.storage.server.entity.TUserInfo;
import com.delicloud.platform.common.lang.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class FileRepoTest {
    @Autowired
    UserRepo userRepo;

    @Autowired
    UploadRepo uploadRepo;

    @Test
    public void toStringTest() {
        TUploadInfo tUploadInfo = uploadRepo.findOne(498443824138289152L);
        log.info("toString : {}", tUploadInfo);
    }

    @Test
    public void test() {
        Long startTime = System.currentTimeMillis();
        log.info("start time : {}", startTime);
        List<TUserInfo> list = new ArrayList<>();
        for (Long i = 1L; i < 10000L; i++) {
            TUserInfo tUserInfo = new TUserInfo();
            tUserInfo.setAccount(11111L);
            list.add(tUserInfo);
//            userRepo.save(tUserInfo);
        }
        userRepo.save(list);
        Long endTime = System.currentTimeMillis();
        log.info("end time : {}", endTime);
        log.info("spend time : {}", endTime - startTime);
    }

    @Test
    public void delete() {
        Long startTime = System.currentTimeMillis();
        log.info("start time : {}", startTime);
        userRepo.deleteAllByAccount(11111L);
        Long endTime = System.currentTimeMillis();
        log.info("end time : {}", endTime);
        log.info("spend time : {}", endTime - startTime);
    }

    private Long[] getToUserIds() {
        return new Long[]{419209879111073792L, 419209879111073793L};
    }

    @Test
    public void test1() {
//        String payload = "{\"from_id\":1123456787,\"to_user_ids\":[419209879111073792,419209879111073793],\"body\":\"{\\\"message\\\":\\\"用户赵匡应同意您的好友申请\\\",\\\"title\\\":\\\"好友申请\\\"}\",\"message_content_type\":0,\"message_type\":1,\"notify_type\":0,\"send_type\":2,\"priority\":1,\"handler_type\":1}";
//        mqMessageSender.send("im_send_msg1", payload);
    }
}