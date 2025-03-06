package com.minwei.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author lmw
 */
@Slf4j
public class UrlUtil {

    /**
     * 从一个URL过滤出特定请求参数并组成一个新URL
     * @param url
     * @param paramsToKeep
     * @return
     */
    public static String filterUrlParam(String url,String... paramsToKeep) {

        try {
            URI uri = new URI(url);
            String query = uri.getQuery();
            if (query == null){
                return url;
            }

            String newQuery = Arrays.stream(query.split("&"))
                    .filter(pair -> {
                        for (String param : paramsToKeep) {
                            if (pair.startsWith(param + "=")) {
                                return true;
                            }
                        }
                        return false;
                    }).collect(Collectors.joining("&"));
            return new URI(uri.getScheme(),uri.getAuthority(),uri.getPath(),newQuery,null).toString();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
