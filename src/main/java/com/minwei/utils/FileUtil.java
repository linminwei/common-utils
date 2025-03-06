package com.minwei.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author lmw
 */
public class FileUtil {

    private static final int THREAD_COUNT = 5;

    public static void downloadImgs(String basePathName,String subPathName,Map<String,String> fileMap) throws Exception{

        // 创建基础目录和子目录
        Path basePath = Paths.get(basePathName);
        Path subPath = basePath.resolve(subPathName);
        if (!Files.exists(subPath)) {
            Files.createDirectories(subPath);
        }

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        for (Map.Entry<String, String> entry : fileMap.entrySet()) {
            String fileName = entry.getKey();
            String fileUrl = entry.getValue();
            // 构建完整的文件保存路径
            Path savePath = subPath.resolve(fileName + ".jpg");
            executor.submit(() ->{
                try {
                    downloadImg(fileUrl,savePath);
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MIN_VALUE, TimeUnit.NANOSECONDS);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void downloadImg(String fileUrl, Path savePath) throws Exception{
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            FileOutputStream fos = new FileOutputStream(String.valueOf(savePath));
            byte[] buffer = new byte[64 * 1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();
            bis.close();
            inputStream.close();
        } else {
            throw new IOException("请求失败，响应码: " + responseCode);
        }
    }
}