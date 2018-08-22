package com.test.mqserver.controller;

import com.test.mqserver.sender.FileSender;
import com.test.mqcore.bo.BaseResp;
import com.test.mqcore.bo.FileResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
public class FileController {
    @Autowired
    FileSender fileSender;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public BaseResp uploadFile(MultipartFile file, Long account, String relativePath) throws Exception {
        System.out.println(relativePath);
        fileSender.uploadFile(file, account, relativePath);
        return new BaseResp<FileResp>(null);
    }

    @RequestMapping(value = "/createFolder")
    public BaseResp createFolder(Long account, String relativePath) throws Exception {
        fileSender.updataFolder(account, relativePath);
        return new BaseResp<FileResp>(null);
    }


}
