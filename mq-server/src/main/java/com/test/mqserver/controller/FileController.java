package com.test.mqserver.controller;

import com.test.mqcore.bo.BaseResp;
import com.test.mqcore.bo.FileResp;
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

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public BaseResp uploadFile(MultipartFile file, Long account, String relativePath) {
        log.debug("uploadFile account is : {} and relativePath is : {}", account, relativePath);
        fileService.uploadFile(file, account, relativePath);
        return new BaseResp<FileResp>(null);
    }

    @RequestMapping(value = "/createFolder")
    public BaseResp createFolder(Long account, String relativePath) {
        log.debug("createFolder account is : {} and relativePath is : {}", account, relativePath);
        fileService.updataFolder(account, relativePath);
        return new BaseResp<FileResp>(null);
    }

    @RequestMapping(value = "/deleteFile")
    public BaseResp deleteFile(String fileName, Long account, String relativePath) {
        log.debug("deleteFile fileName is : {} and account is : {} and relativePath is : {}", fileName, account, relativePath);
        fileService.deleteFile(fileName, account, relativePath);
        return new BaseResp<FileResp>(null);
    }

    @RequestMapping(value = "/deleteFolder")
    public BaseResp deleteFolder(String folderName, Long account, String relativePath) {
        if (!relativePath.equals("")) {
            relativePath = relativePath + "/";
        }
        relativePath = relativePath + folderName;
        log.debug("deleteFile account is : {} and relativePath is : {}", account, relativePath);
        fileService.deleteFolder(account, relativePath);
        return new BaseResp<FileResp>(null);
    }

}
