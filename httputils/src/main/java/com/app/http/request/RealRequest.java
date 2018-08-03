package com.app.http.request;

import com.app.http.CommonParams;
import com.app.http.HttpInterceptor;
import com.app.http.HttpManager;
import com.app.http.callback.CommonCallback;
import com.app.http.error.ErrorModel;
import com.app.http.util.HttpHandler;
import com.app.http.util.Util;

import java.io.EOFException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by thkcheng on 2018/7/16.
 */
public class RealRequest {

    private Request request;

    private CommonParams commonParams;

    private HttpInterceptor httpInterceptor;

    RealRequest(Request request, CommonParams commonParams) {
        this.request = request;
        this.commonParams = commonParams;
        this.httpInterceptor = HttpManager.getInstance().getInterceptor();
    }

    /**
     * 同步执行请求
     * Invokes the request immediately, and blocks until the response can be processed or is in  error.
     *
     * @param callback 回调对象
     * @return 返回结果
     */
    public <T> T execute(CommonCallback<T> callback) {
        httpInterceptor.logRequest(commonParams);
        Call call = HttpManager.getInstance().getOkHttpClient().newCall(request);
        return execute(call, callback);
    }

    /**
     * 异步执行请求
     * Schedules the request to be executed at some point in the future.
     *
     * @param callback 回调对象
     */
    public <T> void enqueue(CommonCallback<T> callback) {
        httpInterceptor.logRequest(commonParams);
        Call call = HttpManager.getInstance().getOkHttpClient().newCall(request);
        callback.requestCommonParams(commonParams, request);
        enqueue(call, callback);
    }


    /**
     * @param call     Call
     * @param callback 回调对象
     * @param <T>      对象的泛型
     */
    private <T> T execute(Call call, final CommonCallback<T> callback) {
        callback.onBefore();
        try {
            Response response = call.execute();
            return onResponseResult(response, callback);
        } catch (IOException ex) {
            ex.printStackTrace();
            onFailureResult(call, ex, callback);
        }
        return null;
    }

    /**
     * @param call     Call
     * @param callback 回调对象
     * @param <T>      对象的泛型
     */
    private <T> void enqueue(Call call, final CommonCallback<T> callback) {
        callback.onBefore();
        if (checkCacheStatus(callback)) {
            return;
        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call,IOException ex) {
                ex.printStackTrace();
                onFailureResult(call, ex, callback);
            }

            @Override
            public void onResponse(Call call, Response response) {
                onResponseResult(response, callback);
            }
        });
    }

    private void onFailureResult(Call call, IOException ex, CommonCallback callback) {
        //{@linkplain okhttp3.RealCall#isCanceled()}
        if (call.isCanceled()) {
            sendCanceledCallback(callback);
        } else {
            // handle failure exception
            ErrorModel errorModel = new ErrorModel(0, "");
            errorModel.setUrl(commonParams.url());
            httpInterceptor.handleFailure(ex, errorModel);
            sendFailureCallback(errorModel, callback);
        }
    }

    private <T> T onResponseResult(Response response, CommonCallback<T> callback) {
        try {
            // 1. check http code
            checkHttpCode(response.code());

            // 2. log response
            Response cloneResponse = httpInterceptor.logResponse(response);

            // 3. parse response
            T result = callback.parseResponse(cloneResponse);

            // 4. call success method
            sendSuccessCallback(result, callback);
            return result;
        } catch (Exception ex) {
            // 1. print stack trace
            ex.printStackTrace();

            // 2. handle response exception
            ErrorModel errorModel = new ErrorModel(0, "");
            errorModel.setUrl(commonParams.url());
            httpInterceptor.handleResponse(ex, errorModel);

            // 3. call fail method
            sendFailureCallback(errorModel, callback);
        }
        return null;
    }

    private <T> T onCacheResult(CommonCallback<T> callback) {
        try {
            // 1. get cache
            String hashkey = Util.urlToCacheKey(request, commonParams.params());

            // 2. get cache json
            String cacheJson = HttpManager.getInstance().getACache().getAsString(hashkey);

            // 3. parse cache
            T result = callback.parseCacheJson(cacheJson);

            // 4. call success method
            sendSuccessCallback(result, callback);
            return result;
        } catch (Exception ex) {
            // 1. print stack trace
            ex.printStackTrace();

            // 2. handle response exception
            ErrorModel errorModel = new ErrorModel(0, "");
            errorModel.setUrl(commonParams.url());
            httpInterceptor.handleResponse(ex, errorModel);

            // 3. call fail method
            sendFailureCallback(errorModel, callback);
        }
        return null;
    }

    /**
     * 检测是否需要获取缓存数据
     *
     * @param callback
     * @param <T>
     */
    private <T> boolean checkCacheStatus(CommonCallback<T> callback) {
        // commonParams.acache is true and cache key->value not null
        if (commonParams.acache()) {
            String hashkey = Util.urlToCacheKey(request, commonParams.params());
            if (HttpManager.getInstance().getACache().getAsString(hashkey) != null) {
                   onCacheResult(callback);
                return true;
            }
        }
        return false;
    }


    /**
     * 检查http code
     *
     * @param code 错误码
     * @throws Exception 自定义网络异常
     */
    private void checkHttpCode(int code) throws Exception {
        if (code < 200 || code >= 300) {// 不是2开头code统一以服务器错误处理
            throw new EOFException("服务器异常:HTTP status code " + code);
        }
    }

    private void sendFailureCallback(final ErrorModel errorModel, final CommonCallback callback) {
        if (errorModel.isProcessed()) { //处理过错误信息，不再回调
            return;
        }
        HttpHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onAfter(false);
                callback.onFailure(errorModel);
            }
        });
    }

    private <T> void sendSuccessCallback(final T object, final CommonCallback<T> callback) {
        HttpHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onAfter(true);
                callback.onSuccess(object);
            }
        });
    }

    private <T> void sendCanceledCallback(final CommonCallback<T> callback) {
        HttpHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onAfter(false);
            }
        });
    }

}
