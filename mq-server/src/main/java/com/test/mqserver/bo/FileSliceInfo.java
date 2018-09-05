package com.test.mqserver.bo;

import lombok.Data;

import java.io.Serializable;

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

    private String tempPath;

    private String filePath;

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

    public Boolean isFinish() {
        for (int i = 0; i < count; i++) {
            if (indexList[i] != 1) {
                return false;
            }
        }
        return true;
    }
}
