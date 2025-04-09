package com.minwei.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

/**
 * @author lmw
 */
public class JsoupUtil {

    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY = 5000; // 5 秒延迟


    public static Document getDocument(String url) {
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                return Jsoup.parse(new URL(url), 20000);
            } catch (IOException e) {
                if (i < MAX_RETRIES - 1) {
                    System.out.println("第 " + (i + 1) + " 次请求失败，等待 " + RETRY_DELAY / 1000 + " 秒后重试...");
                    try {
                        Thread.sleep(RETRY_DELAY);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw new RuntimeException("爬取网址数据异常:" + e.getMessage(), e);
                }
            }
        }
        return null; // 理论上不会执行到这里
    }
}
