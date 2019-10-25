package cn.com.bluemoon.demo.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URLDecoder;

public class FileImgUtils {

    private static String FILE_ROOT = "C://data/crawler";//下载的目标路径

    public static String downImage(String imgurl, String downloadDir, String filename) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(imgurl);
        String saveUrl = "";
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            String reFileName = URLDecoder.decode(filename, "UTF-8");
            saveUrl = writeFile(inputStream, downloadDir , reFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return saveUrl;
    }

    public static String writeFile(InputStream inputStream, String downloadDir, String filename) {
        downloadDir = FILE_ROOT + File.separator + downloadDir;
        String saveUrl = "";
        try {
            //文件保存位置
            File saveDir = new File(downloadDir);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }

            //获取自己数组
            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();

            byte[] getData = bos.toByteArray();
            saveUrl = saveDir + File.separator + filename;
            File file = new File(saveUrl);
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
        return saveUrl;
    }
}
