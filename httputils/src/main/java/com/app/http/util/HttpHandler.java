package com.app.http.util;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by thkcheng on 2018/7/16.
 */
public class HttpHandler {

    private Handler mHandler;

    private HttpHandler() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    private static class Holder {
        private final static HttpHandler INSTANCE = new HttpHandler();
    }

    public static void post(Runnable runnable) {
        Holder.INSTANCE.mHandler.post(runnable);
    }
}
