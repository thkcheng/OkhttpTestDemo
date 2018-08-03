package com.app.http.common;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.app.http.App;
import com.app.http.CommonParams;
import com.app.http.HttpInterceptor;
import com.app.http.error.BIZException;
import com.app.http.error.ErrorModel;
import com.app.http.util.HttpHandler;
import com.google.gson.JsonParseException;

import java.io.EOFException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 拦截器实现类
 * <p>
 * Created by thkcheng on 2018/8/2.
 *
 * 由于TicketApp中网络请求里夹杂了业务
 * 越到后面网络请求的代码越多
 * 所以就加了一个拦截器，把所有的业务写在拦截器里
 * 比如你要对错误统一处理 Login-TimeOut
 * 直接在拦截器里自己实现自己的业务就行了
 * 每个项目的这种类似的业务都会有, 而且还会不尽相同
 *
 * 预计新增：由工厂模式实现解析解耦
 * 工厂模式：https://blog.csdn.net/nugongahou110/article/details/50425823
 *
 */
public final class InterceptorImpl implements HttpInterceptor {

    private String logFormat = "%s %s %s";

    private String TAG = "InterceptorImpl";

    /**
     * 获取固定格式的log
     *
     * @param url    发生错误的url
     * @param desc   错误描述
     * @param result 错误结果
     * @return 固定格式的log
     */
    private String getFormatLog(String url, String desc, String result) {
        return String.format(logFormat, url, desc, result);
    }

    @Override
    public String toJson(Object src) {
        return JsonUtil.toJson(src);
    }

    @Override
    public <T> T convert(String json, Type type) {
        return JsonUtil.toObject(json, type);
    }

    @Override
    public void logRequest(CommonParams commonParams) {
        Map<String, String> params = commonParams.params();
        String paramsStr = commonParams.content();
        if (paramsStr == null || paramsStr.length() == 0) {
            paramsStr = params == null ? "" : params.toString();
        }
        Map<String, String> headers = commonParams.headers();
        String headersStr = headers == null ? "" : headers.toString();
        // 日志格式
        // Request
        // --> https://...
        // --> GET
        // --> {...}
        // --> {...}
        String result = String.format("Request\n >>> %s\n >>> %s\n >>> %s\n >>> %s"
                , commonParams.url()
                , commonParams.method()
                , headersStr
                , paramsStr);
        Log.i(TAG, result);
    }


    @Override
    public ErrorModel handleResponse(Exception why, ErrorModel errorModel) {
        errorModel.setMessage("网络错误");
        if (why instanceof BIZException) { //业务异常
            BIZException cloneWhy = (BIZException) why;
            errorModel.setCode(cloneWhy.getErrorCode())
                    .setMessage(cloneWhy.getErrorMessage())
                    .setResponse(cloneWhy.getResponse());
            errorModel.setResponse(cloneWhy.getResponse());
            Log.e(TAG, getFormatLog(errorModel.getUrl(), errorModel.getResponse(), errorModel.getMessage()));
            return handleBIZ(errorModel);
        } else if (why instanceof EOFException) { //http status code不是200的网络异常
            return errorModel;
        } else if (why instanceof NullPointerException) {
            return errorModel;
        } else if (why instanceof JsonParseException) {
            return errorModel;
        } else {
            return errorModel;
        }
    }

    /**
     * 处理错误的业务
     *
     * @param errorModel ErrorModel
     * @return ErrorModel
     */
    private ErrorModel handleBIZ(final ErrorModel errorModel) {
        HttpHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.getInstance(), errorModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return errorModel;
    }

    @Override
    public ErrorModel handleFailure(Exception why, ErrorModel errorModel) {
        errorModel.setMessage("网络错误");
        if (why instanceof SocketTimeoutException) {
            return errorModel;
        } else {
            return errorModel;
        }
    }

    @Override
    public Response logResponse(Response response) throws Exception {
        Response clone = response.newBuilder().build();
        ResponseBody responseBody = clone.body();
        String url = clone.request().url().toString();
        String body = responseBody.string();
        // 日志格式
        // Response
        // >>> https://...
        // >>> {"code":10,...
        String result = String.format("Response\n >>> %s\n >>> %s", url, body);
        Log.i(TAG, result);

        responseBody = ResponseBody.create(responseBody.contentType(), body);
        return response.newBuilder().body(responseBody).build();
    }

}
