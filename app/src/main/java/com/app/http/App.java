package com.app.http;

import android.app.Application;

import com.app.http.common.InterceptorImpl;
import com.app.http.util.ACache;

/**
 * Created by thkcheng on 2018/7/16.
 */
public class App extends Application {

    public static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        HttpManager.getInstance()
                .setTimeOut(30)
                .setInterceptor(new InterceptorImpl())
                .setCertificates()
                .setACache(ACache.get(this))
                .build();
    }
}
