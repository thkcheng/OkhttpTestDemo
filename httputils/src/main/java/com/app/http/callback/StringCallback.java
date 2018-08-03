package com.app.http.callback;

import com.app.http.HttpManager;
import com.app.http.error.BIZException;
import com.app.http.util.Util;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * Created by thkcheng on 2018/7/16.
 */
public abstract class StringCallback<T> extends CommonCallback<T> {

    private String code = "code";

    private String msg = "message";

    @Override
    public T parseResponse(Response response) throws Exception {
        // 1. print json log
        String json = response.body().string();
        response.close(); //To avoid leaking resources

        // 2. check result code
//        JSONObject jsonObject = new JSONObject(json);
//        int responseCode = jsonObject.optInt(code);
//        String responseMsg = jsonObject.optString(msg);
        JSONObject jsonObject = new JSONObject(json);
        JSONObject result1 = jsonObject.optJSONObject("result");
        int responseCode = result1.optInt(code);
        String responseMsg = result1.optString(msg);

        checkResultCode(responseCode, responseMsg, json);

        // 3. parse json to object
        T result;
        Type type = getType();
        if (type == new TypeToken<String>() {}.getType()) {
            result = (T) json;
        } else {
            result = HttpManager.getInstance().getInterceptor().convert(json, type);
        }
        Util.checkNotNull(result);

        // 4. add cache
        cacheResponse(json);
        return result;
    }

    @Override
    public T parseCacheJson(String cacheJson) throws Exception {
        // 1. parse json to object
        T result;
        Type type = getType();
        if (type == new TypeToken<String>() {}.getType()) {
            result = (T) cacheJson;
        } else {
            result = HttpManager.getInstance().getInterceptor().convert(cacheJson, type);
        }
        Util.checkNotNull(result);

        return result;
    }

    /**
     * 检查返回结果code
     *
     * @param code    错误码
     * @param message 错误信息
     * @throws BIZException 业务异常
     */
    private void checkResultCode(int code, String message, String response) throws BIZException {
        if (code != 0) {// 服务返回结果code不等于0，请求得到的数据有问题
            throw new BIZException(code, message).setResponse(response);
        }
    }

    /**
     * 根据commonParams.acache缓存数据
     *
     * @param json
     */
    private void cacheResponse(String json) {
        if (commonParams.acache()) {
            String hashkey = Util.urlToCacheKey(request, commonParams.params());
            HttpManager.getInstance().getACache().put(hashkey, json, commonParams.time()); //缓存有效时间默认10秒
        }
    }

}
