package org.net.websocket.core.impl.config;

import android.content.Context;

import okhttp3.OkHttpClient;

/**
 * Author: yuzzha
 * Date: 2019-07-15 16:32
 * Description: WebSocket 参数设置 Config 类
 * Remark: 必须设置Context 和 wsUrl  这个两个参数
 */
public class WsConfig {

    private Context mContext;
    private String wsUrl;
    private boolean needReconnect = true;
    private OkHttpClient mOkHttpClient;

    public WsConfig(Context mContext, String wsUrl, boolean needReconnect, OkHttpClient mOkHttpClient) {
        this.mContext = mContext;
        this.wsUrl = wsUrl;
        this.needReconnect = needReconnect;
        this.mOkHttpClient = mOkHttpClient;
    }

    public WsConfig(Builder builder) {
        this.mContext = builder.mContext;
        this.wsUrl = builder.wsUrl;
        this.needReconnect = builder.needReconnect;
        this.mOkHttpClient = builder.mOkHttpClient;
    }

    public WsConfig(Context mContext, String wsUrl) {
        this.mContext = mContext;
        this.wsUrl = wsUrl;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getWsUrl() {
        return wsUrl;
    }

    public void setWsUrl(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    public boolean isNeedReconnect() {
        return needReconnect;
    }

    public void setNeedReconnect(boolean needReconnect) {
        this.needReconnect = needReconnect;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public void setOkHttpClient(OkHttpClient mOkHttpClient) {
        this.mOkHttpClient = mOkHttpClient;
    }

    public static final class Builder {
        private Context mContext;
        private String wsUrl;
        private boolean needReconnect = true;
        private OkHttpClient mOkHttpClient;

        public Builder() {
        }

        public Builder context(Context val) {
            mContext = val;
            return this;
        }

        public Builder wsUrl(String val) {
            wsUrl = val;
            return this;
        }

        public Builder client(OkHttpClient val) {
            mOkHttpClient = val;
            return this;
        }

        public Builder needReconnect(boolean val) {
            needReconnect = val;
            return this;
        }

        public WsConfig build() {
            return new WsConfig(this);
        }
    }
}
