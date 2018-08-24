package com.test.mqcore.bo;

import com.test.mqcore.config.PathConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
public class FileSliceInfo implements Serializable {
    private String idCode;

    private Long fileSize;

    private Integer count;

    private Long sliceSize;

    private String fileName;

    private Long account;

    private String relativePath;

    private int[] indexList;

    public int addFileIndex(int index) {
        if (indexList == null) {
            indexList = new int[count];
            indexList[index] = 1;
            return 0;
        }
        indexList[index] = 1;
        int temp = 0;
        for (int i = 0; i < index; i++) {
            if (indexList[i] == 1) {
                temp++;
            }
        }
        return temp;
    }

    public String getTempPath() {
        return PathConfig.getPath(this.account, this.relativePath) + "/" + idCode + ".tmp";
    }

    public String getFilePath() {
        return PathConfig.getPath(this.account, this.relativePath) + "/" + fileName;
    }

    public Boolean isFinish() {
        for (int i = 0; i < count; i++) {
            if (indexList[i] != 1) {
                return false;
            }
        }
        return true;
    }
}
