package com.app.http.request;

import com.app.http.CommonParams;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by thkcheng on 2018/7/16.
 */

public abstract class OkHttpRequest {

    protected CommonParams commonParams;

    protected Request.Builder requestBuilder = new Request.Builder();

    protected String buildUrl() {
        return commonParams.url();
    }

    protected abstract RequestBody buildRequestBody();

    protected abstract Request buildRequest(RequestBody requestBody);

    protected RequestBody wrapRequestBody(RequestBody requestBody) {
        return requestBody;
    }

    public Request generateRequest() {
        requestBuilder.tag(commonParams.tag())
                .url(buildUrl())
                .headers(buildHeaders());
        RequestBody requestBody = wrapRequestBody(buildRequestBody());
        return buildRequest(requestBody);
    }

    private Headers buildHeaders() {
        Map<String, String> headers = commonParams.headers();
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers != null && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                headerBuilder.add(key, headers.get(key));
            }
        }
        return headerBuilder.build();
    }

    public RealRequest build(CommonParams commonParams) {
        this.commonParams = commonParams;
        //开始构建请求
        return new RealRequest(generateRequest(), commonParams);
    }

}
