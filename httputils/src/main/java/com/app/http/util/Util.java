package com.app.http.util;

import java.io.Closeable;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by thkcheng on 2018/8/2.
 */
public class Util {

    private Util() {
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }


    /**
     * 把URL和参数转为hashCode为Acache的key
     *
     * @param request 请求
     * @param paramsMap post请求的参数map
     * @return hashCode
     */
    public static String urlToCacheKey(Request request, Map<String, String> paramsMap) {
        try {
            String method = request.method();
            if (method.contains("GET")) {
                return "" + request.url().hashCode();
            }
            if (method.contains("POST")) {
                paramsMap.put("url", request.url().toString());
                return "" + paramsMap.hashCode();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
