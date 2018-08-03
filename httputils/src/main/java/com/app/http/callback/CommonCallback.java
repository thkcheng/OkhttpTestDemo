package com.app.http.callback;

import com.app.http.CommonParams;
import com.app.http.error.ErrorModel;
import com.app.http.util.ACache;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求回调类
 * @param <T> 解析的对象
 *
 * Created by thkcheng on 2018/7/16.
 */
public abstract class CommonCallback<T> {

    protected CommonParams commonParams;
    protected Request request;

    /**
     * PostFormRequest -> wrapRequestBody()
     */
    public final static CommonCallback<String> NO_CALLBACK = new StringCallback<String>() {
        @Override
        public void onSuccess(String response, Object... obj) {
        }

        @Override
        public void onFailure(ErrorModel errorModel) {
        }
    };

    /**
     * 开始执行网络请求
     */
    public void onBefore() {
    }

    /**
     * parse {@link Response}
     *
     * @param response {@link Response}
     * @throws Exception
     */
    public abstract T parseResponse(Response response) throws Exception;

    /**
     * 解析本地缓存的JSON数据
     *
     * @param cacheJson {@link ACache}
     * @throws Exception
     */
    public abstract T parseCacheJson(String cacheJson) throws Exception;

    /**
     * @param response 返回的对象
     * @param obj      可扩展参数
     */
    public abstract void onSuccess(T response, Object... obj);

    /**
     * @param errorModel 错误信息
     */
    public abstract void onFailure(ErrorModel errorModel);

    /**
     * 网络请求结束
     *
     * @param success 请求状态标记，true表示请求成功结果正确
     */
    public void onAfter(boolean success) {
    }

    /**
     * @param progress 进度
     * @param total    总长度
     */
    public void inProgress(float progress, long total) {
    }

    /**
     * 获取Json对象的类型，因为数据可能是Json数组也可能是Json对象
     */
    public Type getType() {
        Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (type instanceof Class) {//如果是Object直接返回
            return type;
        } else {//如果是集合，获取集合的类型map或list
            return new TypeToken<T>() {
            }.getType();
        }
    }

    /**
     * 网络请求参数
     *
     * @param commonParams 请求参数
     */
    public void requestCommonParams (CommonParams commonParams, Request request) {
        this.commonParams = commonParams;
        this.request = request;
    }

}
