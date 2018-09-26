package com.delicloud.platform.cloud.storage.core.config;

import com.delicloud.platform.common.lang.exception.PlatformException;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(RemoteConfig.SERVICE_NAME)
@EnableFeignClients(basePackages = { "com.delicloud.platform.cloud.storage.client" })
public class RemoteConfig {

	public final static String SERVICE_NAME = "tlf";

}
