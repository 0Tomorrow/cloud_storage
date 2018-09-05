package com.test.cloud_storage;

import com.test.mqserver.CloudStorageApplication;
import com.test.mqserver.bo.FileSliceInfo;
import com.test.mqserver.cache.FileSliceCache;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudStorageApplication.class)
@Slf4j
public class CloudStorageApplicationTests {

	@Autowired
	FileSliceCache fileSliceCache;

	@Test
	public void contextLoads() {
		FileSliceInfo fileSliceInfo = fileSliceCache.getFileSliceInfo("159278602041536137088687");
		System.out.println(fileSliceInfo);
	}

}
