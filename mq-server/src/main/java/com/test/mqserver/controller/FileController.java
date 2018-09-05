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
        fileService.mqUploadSliceFile(file, index, idCode);
        return new BaseResp<FileResp>(null);
    }

    @RequestMapping(value = "/deleteFile")
    public BaseResp deleteFile(String fileName, Long account, String relativePath) {
        log.info("deleteFile fileName is : {} and account is : {} and relativePath is : {}", fileName, account, relativePath);
        fileService.mqDeleteFile(fileName, account, relativePath);
        return new BaseResp<FileResp>(null);
    }

}
