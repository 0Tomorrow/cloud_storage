package com.delicloud.platform.cloudstorageclient.impl;

import com.delicloud.platform.cloud.storage.core.bo.FileInfo;
import com.delicloud.platform.cloud.storage.core.bo.FileInfoResp;
import com.delicloud.platform.cloud.storage.core.bo.IndexInfo;
import com.delicloud.platform.cloud.storage.core.bo.IndexInfoResp;
import com.delicloud.platform.cloud.storage.core.config.RemoteConfig;
import com.delicloud.platform.cloudstorageclient.impl.fallback.ImClientFallbackFactory;
import com.delicloud.platform.common.lang.bo.RespBase;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = RemoteConfig.SERVICE_NAME, fallback = ImClientFallbackFactory.class)
public interface StorageClient {

    @RequestMapping(value = "/file/show", method = RequestMethod.GET)
    RespBase<List<FileInfoResp>> showFile(Long account, String path);

    @RequestMapping(value = "/file/handShake", method = RequestMethod.POST)
    RespBase<String> handShake(@RequestBody FileInfo fileInfo);

    @RequestMapping(value = "/file/sliceUpload", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    RespBase<Void> sliceUpload(MultipartFile file, Integer index, String mergeCode);

    @RequestMapping(value = "/file/delete", method = RequestMethod.POST)
    RespBase<Void> deleteFile(String path, String fileName, Long account);

    @RequestMapping(value = "/index/show", method = RequestMethod.GET)
    RespBase<List<IndexInfoResp>> showIndex(@RequestBody IndexInfo indexInfo);

    @RequestMapping(value = "/index/create", method = RequestMethod.POST)
    RespBase<Void> createIndex(@RequestBody IndexInfo indexInfo);

    @RequestMapping(value = "/index/delete", method = RequestMethod.POST)
    RespBase<Void> deleteIndex(@RequestBody IndexInfo indexInfo, boolean sure);
}
