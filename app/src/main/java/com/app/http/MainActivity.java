package com.app.http;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.callback.StringCallback;
import com.app.http.common.Apis;
import com.app.http.error.ErrorModel;
import com.app.http.model.IndexModel;
import com.app.http.model.RecommendModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    TextView tvData;

    public static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        tvData = findViewById(R.id.tvData);

        //两秒钟之内只取一个点击事件,防多次点击
        RxView.clicks(findViewById(R.id.btn2)).throttleFirst(2, TimeUnit.MILLISECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                postRecommend();
            }
        });
    }

    public void getIndex(View view) {
        HttpManager.get(Apis.APP_INDEX)
                .tag(this)
                .acache(true) //开启缓存,默认缓存10s
                .build()
                .enqueue(new StringCallback<IndexModel>() {

                    @Override
                    public void onSuccess(IndexModel response, Object... obj) {
                        tvData.setText(response.getData().toString());
                    }

                    @Override
                    public void onFailure(ErrorModel errorModel) {
                        tvData.setText(errorModel.getMessage());
                    }
                });
    }

    public void postRecommend() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("site_id", 1 + "");
        HttpManager.post(Apis.APP_RECOMEND)
                .tag(this)
                .params(params)
                .acache(true, 30) //开启缓存,缓存30s
                .build()
                .enqueue(new StringCallback<RecommendModel>() {
                    @Override
                    public void onSuccess(RecommendModel response, Object... obj) {
                        tvData.setText(response.getData().toString());
                    }

                    @Override
                    public void onFailure(ErrorModel errorModel) {
                        tvData.setText(errorModel.getMessage());
                    }
                });
    }

    public void getProduct(View view) {

    }

}
