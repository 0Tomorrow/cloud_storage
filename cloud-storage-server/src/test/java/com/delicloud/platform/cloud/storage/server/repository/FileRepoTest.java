package com.delicloud.platform.cloud.storage.server.repository;

import com.delicloud.platform.cloud.storage.server.Application;
import com.delicloud.platform.cloud.storage.server.entity.TIndexInfo;
import com.delicloud.platform.cloud.storage.server.entity.TUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class FileRepoTest {
    @Autowired
    UserRepo userRepo;

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
}