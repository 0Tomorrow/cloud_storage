package com.test.mqservice.util;

import com.test.mqcore.bo.FileSliceInfo;
import com.test.mqcore.config.ErrorCode;
import com.test.mqcore.config.SpringContextProvider;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
    public static void createTempFile(FileSliceInfo fileSliceInfo) {
        String tempPath = fileSliceInfo.getTempPath();
        File file = new File(tempPath);
        File temp = new File(tempPath + ".tmp");
        if (file.exists()) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        try {
            if (!file.createNewFile()) {
                throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
            }
            if (!temp.createNewFile()) {
                throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void uploadFinish(FileSliceInfo fileSliceInfo) {
        if (!fileSliceInfo.isFinish()) {
            return;
        }
        String tempPath = fileSliceInfo.getTempPath();
        String filePath = fileSliceInfo.getFilePath();
        File file = new File(tempPath);
        if (!file.renameTo(new File(filePath))) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
        File temp = new File(tempPath + ".tmp");
        if (!temp.delete()) {
            throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
        }
    }

    public static FileSliceInfo merge(byte[] file, int index, FileSliceInfo fileSliceInfo) {
        String tempPath = fileSliceInfo.getTempPath();
        Long sliceSize = fileSliceInfo.getSliceSize();
        int fileIndex = fileSliceInfo.addFileIndex(index);
        long pos = sliceSize * fileIndex;
        insert(file, pos, tempPath);
        uploadFinish(fileSliceInfo);
        return fileSliceInfo;
    }

    public static void insert(byte[] file, long pos, String path) {
        File tempFile = new File(path);
        try (RandomAccessFile raf = new RandomAccessFile(tempFile, "rw")) {
            File temp = new File(path + ".tmp");
            FileInputStream fileInputStream = new FileInputStream(temp);
            FileOutputStream fileOutputStream = new FileOutputStream(temp);
            raf.seek(pos);
            byte[] buff = new byte[64];
            int hasRead = 0;
            while((hasRead = raf.read(buff)) > 0){
                fileOutputStream.write(buff);
            }
            raf.seek(pos);
            raf.write(file);
            //追加文件插入点之后的内容
            while((hasRead = fileInputStream.read(buff)) > 0){
                raf.write(buff, 0, hasRead);
            }
            raf.close();
            fileInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String uploadFile(byte[] file, String filePath, String fileName) {
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        while (new File(filePath + "/" + fileName + fileType).exists()) {
            String regex = "^(.*)\\((\\d+)\\)$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(fileName); // 获取 matcher 对象
            if (!m.find()) {
                fileName = fileName + "(1)";
                continue;
            }
            int index = Integer.parseInt(m.group(2)) + 1;
            fileName = m.group(1) + "(" + index + ")";
        }
        String path = filePath + "/" + fileName + fileType;
        saveFile(file, path);
        return fileName + fileType;
    }

    public static void saveFile(byte[] file, String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File streamFile = new File(path);
                    FileOutputStream fop = new FileOutputStream(streamFile);
//                    OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
                    fop.write(file);
//                    writer.close();
                    fop.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void deleteFile(String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
                    }
                    if (!file.delete()) {
                        throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void deleteFolder(String folderPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(folderPath);
                    if (!file.exists()) {
                        throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
                    }
                    if (!file.isDirectory()) {
                        throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
                    }
                    if (!file.delete()) {
                        throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
