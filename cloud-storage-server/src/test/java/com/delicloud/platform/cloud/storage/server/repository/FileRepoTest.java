package com.delicloud.platform.cloud.storage.server.repository;

import com.delicloud.platform.cloud.storage.server.Application;
import com.delicloud.platform.cloud.storage.server.entity.TIndexInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class FileRepoTest {
    @Autowired
    IndexRepo indexRepo;

    @Test
    public void test() {
        File file = new File("https://test-delicloud.oss-cn-shanghai.aliyuncs.com/oss-1537495993478-1500.jpg");
        log.info("文件：", file);
    }
}