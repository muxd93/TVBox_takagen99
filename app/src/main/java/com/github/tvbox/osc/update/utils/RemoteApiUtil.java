package com.github.tvbox.osc.update.utils;

import android.content.Context;
import android.widget.Toast;

import com.github.tvbox.osc.util.LogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

public class RemoteApiUtil {
    private static RemoteApiUtil instance;

    public static RemoteApiUtil get() {
        if (instance == null) {
            synchronized (RemoteApiUtil.class) {
                if (instance == null) {
                    instance = new RemoteApiUtil();
                }
            }
        }
        return instance;
    }

    public void doGet(String url, RemoteApiConfigCallback callback) {
        LogUtil.i("request url : " + url);
        OkGo.<String>get(url)
                .headers("User-Agent", "okhttp/3.15")
                .headers("Accept", "text/html," + "application/xhtml+xml,application/xml;q=0.9,image/avif," + "image/webp,image/apng," + "*/*;q=0.8,application/signed-exchange;v=b3;" + "q=0.9")
                .execute(new StringCallback() {
            @Override
            public void onError(Response<String> response) {
                callback.error("请求失败，没有获取到数据！！");
            }

            @Override
            public void onSuccess(Response<String> response) {
                callback.success(response.body());
            }
        });
    }

    public void Subscribe(Context context, String url) {

        Toast.makeText(context, "开始获取订阅，网络慢的话可能需要较长时间！！！", Toast.LENGTH_SHORT).show();

        // 处理节点
        RemoteApiUtil.get().doGet(url, new RemoteApiConfigCallback() {
            @Override
            public void success(String sourceJson) {
                Toast.makeText(context, "订阅获取成功. " + sourceJson, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void error(String msg) {
                Toast.makeText(context, "订阅获取失败. " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface RemoteApiConfigCallback {
        void success(String json);

        void error(String msg);
    }
}
