package com.test.mqserver.controller;

import com.test.mqserver.bo.BaseResp;
import com.test.mqserver.bo.FileResp;
import com.test.mqserver.service.FolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FolderController {

    @Autowired
    FolderService folderService;

    @RequestMapping(value = "/deleteFolder")
    public BaseResp deleteFolder(String folderName, Long account, String relativePath) {
        if (!relativePath.equals("")) {
            relativePath = relativePath + "/";
        }
        relativePath = relativePath + folderName;
        log.info("deleteFile account is : {} and relativePath is : {}", account, relativePath);
        folderService.mqDeleteFolder(account, relativePath);
        return new BaseResp<FileResp>(null);
    }

    @RequestMapping(value = "/createFolder")
    public BaseResp createFolder(String folderName, Long account, String relativePath) {
        log.info("createFolder account is : {} and relativePath is : {}", account, relativePath);
        folderService.updateFolder(account, relativePath);
        return new BaseResp<FileResp>(null);
    }
}
