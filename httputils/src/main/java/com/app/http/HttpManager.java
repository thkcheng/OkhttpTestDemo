package com.app.http;

import android.content.Context;

import com.app.http.util.ACache;
import com.app.http.util.HttpsUtils;

import java.io.InputStream;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * HTTP请求管理者
 * <p>
 * Created by thkcheng on 2018/7/16.
 */
public class HttpManager {

    public static final String TAG = "HttpManager";

    /**
     * 超时时间
     */
    private static final int TIMEOUT = 30;

    /**
     * OkHttpClient 只有一个实例
     */
    private OkHttpClient okHttpClient;

    /**
     * OkHttpClient.Builder
     */
    private OkHttpClient.Builder okHttpClientBuilder;

    private HttpInterceptor interceptor = HttpInterceptor.NO_INTERCEPTOR;

    /**
     * 本地缓存
     */
    private ACache aCache = null;

    /**
     * 单例
     *
     * @return HttpManager
     */
    public static HttpManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /**
         * HttpManager 只有一个实例
         */
        private final static HttpManager INSTANCE = new HttpManager();
    }

    private HttpManager() {
        okHttpClientBuilder = new OkHttpClient.Builder();
        timeOut(TIMEOUT);
        build();
    }

    private void timeOut(long timeOut) {
        okHttpClientBuilder.retryOnConnectionFailure(true)   //连接失败后是否重新连接
                .connectTimeout(timeOut, TimeUnit.SECONDS)   //从主机读取数据超时
                .readTimeout(timeOut, TimeUnit.SECONDS)      //从主机读数据超时
                .writeTimeout(timeOut, TimeUnit.SECONDS);     //从主机写数据超时
//                .proxy(Proxy.NO_PROXY);                      //禁止使用代理
    }

    public void build() {
        okHttpClient = okHttpClientBuilder.build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        this.okHttpClientBuilder = okHttpClient.newBuilder();
    }

    public HttpInterceptor getInterceptor() {
        return interceptor;
    }

    public HttpManager setInterceptor(HttpInterceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    public ACache getACache() {
        return aCache;
    }

    /**
     * 开启本地缓存总闸
     * 不注册该方法默认不使用本地缓存
     */
    public HttpManager setACache(ACache aCache) {
        this.aCache = aCache;
        return this;
    }

    /**
     * 设置超时时间 单位秒
     *
     * @param timeOut 超时时间 默认30秒
     * @return HttpManager
     */
    public HttpManager setTimeOut(long timeOut) {
        timeOut(timeOut);
        return this;
    }

    /**
     * https单向认证
     * 用含有服务端公钥的证书校验服务端证书
     */
    public HttpManager setCertificates(InputStream... certificates) {
        setCertificates(null, null, certificates);
        return this;
    }

    /**
     * https双向认证
     *
     * @param bksFile      bks证书
     * @param password     the password used to check the integrity of the keystore, the password used to unlock the keystore or {@code null}
     * @param certificates 用含有服务端公钥的证书校验服务端证书
     */
    public HttpManager setCertificates(InputStream bksFile, String password, InputStream... certificates) {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, bksFile, password, certificates);
        if (sslParams != null) {
            okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        }
        return this;
    }


    /**
     * GET 请求
     */
    public static CommonParams.Builder get(String url) {
        return newBuilder(CommonParams.GET, url);
    }

    /**
     * POST
     */
    public static CommonParams.Builder post(String url) {
        return newBuilder(CommonParams.POST_FORM, url);
    }

    /**
     * POST-String 请求
     */
    public static CommonParams.Builder postString(String url) {
        return newBuilder(CommonParams.POST_STRING, url);
    }

    /**
     *
     * @param method 请求方式
     * @return CommonParams.Builder->OkHttpRequest
     */
    private static CommonParams.Builder newBuilder(String method, String url) {
        return new CommonParams.Builder(method).url(url);
    }

    /**
     * 根据tag取消请求
     */
    public void cancleTag(Object tag) {
        OkHttpClient client = getInstance().getOkHttpClient();
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
}
