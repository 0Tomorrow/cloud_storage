package com.test.mqserver.controller;

import com.test.mqserver.service.ShowService;
import com.test.mqserver.bo.BaseResp;
import com.test.mqserver.entity.FileInfo;
import com.test.mqserver.entity.IndexInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class ShowController {

    @Autowired
    ShowService showService;

    @RequestMapping(value = "showFile")
    public BaseResp showFile(String folderName, Long account, String relativePath) {
        List<FileInfo> fileList = showService.showFile(folderName, account, relativePath);
        return new BaseResp<List<FileInfo>>(fileList);
    }

    @RequestMapping(value = "showFolder")
    public BaseResp showFolder(String folderName, Long account, String relativePath) {
        List<IndexInfo> indexList = showService.showFolder(folderName, account, relativePath);
        return new BaseResp<List<IndexInfo>>(indexList);
    }
}
