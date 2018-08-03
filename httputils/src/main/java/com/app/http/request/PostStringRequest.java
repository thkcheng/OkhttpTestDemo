package com.app.http.request;

import com.app.http.HttpManager;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by thkcheng on 2018/7/16.
 */
public class PostStringRequest extends OkHttpRequest {

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(commonParams.mediaType(), getContent());
    }

    private String getContent() {
        //优先使用content字段，content为空则使用params字段
        String content = commonParams.content();
        if (content == null || content.length() == 0) {
            content = HttpManager.getInstance().getInterceptor().toJson(commonParams.params());
        }
        return content;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return requestBuilder.post(requestBody).build();
    }
}
