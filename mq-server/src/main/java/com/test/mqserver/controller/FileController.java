package com.test.mqserver.controller;

import com.test.mqserver.bo.BaseResp;
import com.test.mqserver.bo.FileResp;
import com.test.mqserver.bo.FileSliceInfo;
import com.test.mqserver.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@Slf4j
public class FileController {
    @Autowired
    FileService fileService;

    @RequestMapping(value = "/handShake", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded;charset=UTF-8"})
    public BaseResp handShake(FileSliceInfo sliceInfo) {
        fileService.handShake(sliceInfo);
        return new BaseResp<FileResp>(null);
    }

    @RequestMapping(value = "/uploadSliceFile", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public BaseResp uploadSliceFile(MultipartFile file, @RequestParam("index") int index, @RequestParam("idCode") String idCode) {
        fileService.uploadSliceFile(file, index, idCode);
        return new BaseResp<FileResp>(null);
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded;charset=UTF-8"})
    public BaseResp uploadFile(MultipartFile file, Long account, String relativePath) {
        log.info("uploadFile account is : {} and relativePath is : {}", account, relativePath);
        fileService.uploadFile(file, account, relativePath);
        return new BaseResp<FileResp>(null);
    }

    @RequestMapping(value = "/createFolder")
    public BaseResp createFolder(Long account, String relativePath) {
        log.info("createFolder account is : {} and relativePath is : {}", account, relativePath);
        fileService.updataFolder(account, relativePath);
        return new BaseResp<FileResp>(null);
    }

    @RequestMapping(value = "/deleteFile")
    public BaseResp deleteFile(String fileName, Long account, String relativePath) {
        log.info("deleteFile fileName is : {} and account is : {} and relativePath is : {}", fileName, account, relativePath);
        fileService.deleteFile(fileName, account, relativePath);
        return new BaseResp<FileResp>(null);
    }

    @RequestMapping(value = "/deleteFolder")
    public BaseResp deleteFolder(String folderName, Long account, String relativePath) {
        if (!relativePath.equals("")) {
            relativePath = relativePath + "/";
        }
        relativePath = relativePath + folderName;
        log.info("deleteFile account is : {} and relativePath is : {}", account, relativePath);
        fileService.deleteFolder(account, relativePath);
        return new BaseResp<FileResp>(null);
    }

}
