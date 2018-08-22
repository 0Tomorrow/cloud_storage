package com.test.mqcore.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUtil {
    public static StringBuffer fileToStringBuffer(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String prefix=fileName.substring(fileName.lastIndexOf("."));
        File streamFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), prefix);
        file.transferTo(streamFile);

        FileInputStream fip = new FileInputStream(streamFile);
        InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
        StringBuffer sb = new StringBuffer();
        while (reader.ready()) {
            sb.append((char) reader.read());
        }
        reader.close();
        fip.close();
        deleteFile(streamFile);
        return sb;
    }

    private static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

}
