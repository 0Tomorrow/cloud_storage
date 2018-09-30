package com.delicloud.platform.cloud.storage.server;

import com.delicloud.platform.cloud.storage.server.repository.IndexRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTests {

	@Test
	public void contextLoads() {
		RestTemplate restTemplate = new RestTemplate();

		String result = restTemplate.getForObject("http://192.168.0.202:8088/app/mock/34/getVersion?type=12321321321321421", String.class);
		System.out.println(result);
	}

}
