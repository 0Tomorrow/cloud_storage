package com.delicloud.platform.cloudstorageclient.impl.fallback;

import com.delicloud.platform.cloudstorageclient.impl.StorageClient;
import com.delicloud.platform.common.service.hystrix.PlatformHystrixClientFallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ImClientFallbackFactory extends PlatformHystrixClientFallbackFactory<StorageClient> {

}
