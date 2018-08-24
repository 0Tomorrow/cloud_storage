package com.test.mqcore.bo;

import com.test.mqcore.util.FileUtil;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class FileSliceBo implements Serializable {

    private int index;

    private byte[] file;

    private String idCode;

    private FileSliceInfo fileSliceInfo;

    public FileSliceBo(){

    }

    public FileSliceBo(MultipartFile file, int index, String idCode) {
        try {
            this.file = FileUtil.fileToStringBuffer(file, file.getSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.index = index;
        this.idCode = idCode;
    }
}
