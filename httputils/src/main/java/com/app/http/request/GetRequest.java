package com.app.http.request;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by thkcheng on 2018/7/16.
 */
public class GetRequest extends OkHttpRequest {

    @Override
    protected String buildUrl() {
        String url = commonParams.url();
        Map<String, String> params = commonParams.params();
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return requestBuilder.get().build();
    }
}
