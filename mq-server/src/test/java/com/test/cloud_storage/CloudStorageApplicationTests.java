package com.test.cloud_storage;

import com.test.mqserver.CloudStorageApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudStorageApplication.class)
@Slf4j
public class CloudStorageApplicationTests {

	@Test
	public void contextLoads() {
		log.info("debug");
	}

}
