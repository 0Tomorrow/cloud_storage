package com.delicloud.platform.cloud.storage.server.controller;

import com.delicloud.platform.cloud.storage.core.bo.FileInfo;
import com.delicloud.platform.cloud.storage.core.bo.FileInfoResp;
import com.delicloud.platform.cloud.storage.server.aop.Token;
import com.delicloud.platform.cloud.storage.server.bo.FileReq;
import com.delicloud.platform.cloud.storage.server.bo.PdfImgInfo;
import com.delicloud.platform.cloud.storage.server.entity.TUserInfo;
import com.delicloud.platform.cloud.storage.server.service.FileService;
import com.delicloud.platform.common.lang.bo.RespBase;
import com.delicloud.platform.common.lang.exception.PlatformException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {
    @Autowired
    FileService fileService;

//    @Token
    @ApiOperation(value = "通过path查询该路径下的所有文件", response = RespBase.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public RespBase<List<FileInfoResp>> show(Long account, String path) {
        List<FileInfoResp> list = fileService.findFile(account, path);
        return new RespBase<>(list);
    }

    @ApiOperation(value = "上传文件前的握手", response = RespBase.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/handShake", method = RequestMethod.POST)
    public RespBase<String> handShake(@RequestBody FileInfo fileInfo) {
        log.info("start handShake : {}", fileInfo);
        String mergeCode = fileService.handShake(fileInfo);
        return new RespBase<>(mergeCode);
    }

    @ApiOperation(value = "文件分片上传", response = RespBase.class, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequestMapping(value = "/sliceUpload", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public RespBase sliceUpload(MultipartFile file, Integer index, String mergeCode) {
        fileService.mqMerge(file, index, mergeCode);
        return RespBase.OK_RESP_BASE;
    }

    @ApiOperation(value = "删除文件", response = RespBase.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespBase deleteFile(String id) {
        fileService.deleteFile(id);
        return RespBase.OK_RESP_BASE;
    }

    @ApiOperation(value = "预览文件", response = RespBase.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/preview", method = RequestMethod.POST)
    public RespBase<List<PdfImgInfo>> preview(String id) {
        List<PdfImgInfo> imgList = fileService.preview(id);
        return new RespBase<>(imgList);
    }

    @ApiOperation(value = "下载文件", response = RespBase.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "download", method = RequestMethod.POST)
    public RespBase<String> download(String id) {
        String downloadPath = fileService.getDownloadPath(id);
        return new RespBase<>(downloadPath);
    }

}
