package com.test.mqcore.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;

public class FileUtil {

    public static byte[] fileToStringBuffer(MultipartFile file, Long byteSize) throws Exception {
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


}
