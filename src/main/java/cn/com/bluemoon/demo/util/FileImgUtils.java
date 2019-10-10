package cn.com.bluemoon.demo.util;

import java.io.*;
import java.net.MalformedURLException;

public class FileImgUtils {

    public static void writeFile(InputStream inputStream, String downloadDir, String filename) {
        try {
            //获取自己数组
            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();

            byte[] getData = bos.toByteArray();

            //文件保存位置
            File saveDir = new File(downloadDir);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + filename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
