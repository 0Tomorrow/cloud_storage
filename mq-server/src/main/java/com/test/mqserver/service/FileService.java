package com.test.mqserver.service;

import com.test.mqcore.bo.FileBo;
import com.test.mqserver.sender.FileSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Autowired
    FileSender fileSender;

    public void uploadFile(MultipartFile file, Long account, String relativePath) {
        FileBo fileBo = new FileBo(file, account, relativePath);
        fileSender.uploadFile(fileBo);
    }

    public void updataFolder(Long account, String relativePath) {
        FileBo fileBo = new FileBo(account, relativePath);
        fileSender.updataFolder(fileBo);
    }

    public void deleteFile(String fileName, Long account, String relativePath) {
        FileBo fileBo = new FileBo(fileName, account, relativePath);
        fileSender.deleteFile(fileBo);
    }

    public void deleteFolder(Long account, String relativePath) {
        FileBo fileBo = new FileBo(account, relativePath);
        fileSender.deleteFolder(fileBo);
    }
}
