package com.github.tvbox.osc.server;

/**
 * @author pj567
 * @date :2021/1/5
 * @description:
 */
public interface DataReceiver {

    /**
     * @param text
     */
    void onTextReceived(String text);

    void onApiReceived(String url);

    void onStoreReceived(String url);

    void onLiveReceived(String url);

    void onEpgReceived(String url);

    void onProxysReceived(String url);

    void onPushReceived(String url);

    void onMirrorReceived(String id, String sourceKey);

    void onProxyReceived(String url);
}