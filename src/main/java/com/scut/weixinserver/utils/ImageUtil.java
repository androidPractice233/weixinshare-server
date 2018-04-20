package com.scut.weixinserver.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class ImageUtil {

    public static String saveImg(MultipartFile multipartFile, String path) throws IOException {
        File file = new File(path);
        if(!file.exists()) {
            file.mkdir();
        }

        FileInputStream fileInputStream = (FileInputStream) multipartFile.getInputStream();
        String fileType = multipartFile.getContentType().substring(6);
        String fileName = Uuid.getUuid() + "." + fileType;
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                new FileOutputStream(path + File.separator + fileName));
        byte[] bs = new byte[1024];
        int len;
        while((len = fileInputStream.read(bs)) != -1) {
            bufferedOutputStream.write(bs, 0, len);
        }
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        fileInputStream.close();
        return fileName;
    }

    public static boolean deleteImg(String path, String fileName) {
        File file = new File(path + fileName);
        if(!file.exists()) {
            return false;
        }else {
            file.delete();
            return true;
        }
    }

}
