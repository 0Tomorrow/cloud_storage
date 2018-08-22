package com.test.mqservice.util;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
    public static String uploadFile(StringBuffer file, String filePath, String fileName) throws Exception {
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        while (new File(filePath + fileName + fileType).exists()) {
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

    public static void saveFile(StringBuffer file, String path) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File streamFile = new File(path);
                    FileOutputStream fop = new FileOutputStream(streamFile);
                    OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
                    writer.append(file);
                    writer.close();
                    fop.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
