package com.test.mqserver.controller;

import com.test.mqserver.service.ShowService;
import com.test.mqcore.bo.BaseResp;
import com.test.mqcore.entity.FileInfo;
import com.test.mqcore.entity.IndexInfo;
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
    public BaseResp showFile(Long account, String relativePath) {
        List<FileInfo> fileList = showService.showFile(account, relativePath);
        return new BaseResp<List<FileInfo>>(fileList);
    }

    @RequestMapping(value = "showFolder")
    public BaseResp showFolder(Long account, String relativePath) {
        List<IndexInfo> indexList = showService.showFolder(account, relativePath);
        return new BaseResp<List<IndexInfo>>(indexList);
    }
}
