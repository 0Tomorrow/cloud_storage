package com.test.mqserver.util;

import com.test.mqserver.bo.FileSliceInfo;
import com.test.mqserver.config.ErrorCode;
import com.test.mqserver.config.SpringContextProvider;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;

public class FileUtil {
    public static byte[] fileToByteBuffer(MultipartFile file, Long byteSize) throws Exception {
        InputStream fip = file.getInputStream();
        byte[] data = new byte[1024];
        ByteBuffer buffer = ByteBuffer.allocate(Integer.parseInt(byteSize + ""));
        int fileSize = 0;
        int len;
        while ((len = fip.read(data)) != -1) {
            fileSize += len;
            buffer.put(data,0,len);
        }
        fip.close();
        ByteBuffer fileBuffer = ByteBuffer.allocate(fileSize);
        data = fileBuffer.put(buffer.array(), 0, fileSize).array();
        return data;
    }

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

    private static void uploadFinish(FileSliceInfo fileSliceInfo) {
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

    private static void insert(byte[] file, long pos, String path) {
        File tempFile = new File(path);
        try (RandomAccessFile raf = new RandomAccessFile(tempFile, "rw")) {
            File temp = new File(path + ".tmp");
            FileInputStream fileInputStream = new FileInputStream(temp);
            FileOutputStream fileOutputStream = new FileOutputStream(temp);
            raf.seek(pos);
            byte[] buff = new byte[64];
            int hasRead;
            while(raf.read(buff) > 0){
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

    public static void deleteFile(String filePath) {
        new Thread(() -> {
            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
                }
                if (!file.isFile()) {
                    throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
                }
                if (!file.delete()) {
                    throw SpringContextProvider.createPlatformException(ErrorCode.FolderPathFormatError);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void deleteFolder(String folderPath) {
        new Thread(() -> {
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
        }).start();
    }
}
