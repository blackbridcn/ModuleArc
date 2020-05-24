package org.net.http.rxretrofit.api;

import android.content.Context;

import org.net.http.rxretrofit.utils.Utils;
import org.utils.LogUtils;

public class BaseUrl {

    private static class BaseUrlHolder {
        private static final BaseUrl INSTANCE = new BaseUrl();
    }

    private String UserAgent;

    private BaseUrl() {
    }

    public void initDebugInfo(Context mContext) {
        String versionCode = Utils.getAppVersionCode(mContext);
        String versionName = Utils.getAppVersionName(mContext);
        this.UserAgent = "Android-PeakeCloud-" + versionName + "-" + versionCode;
    }

    public static BaseUrl getInstance() {
        return BaseUrlHolder.INSTANCE;
    }


    private String baseUrl = "https://app.peake.com.cn";


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        if (Utils.isNotEquals(this.baseUrl, baseUrl)) {
            this.baseUrl = baseUrl;
        }
        LogUtils.i("TAG", "--------->baseUrl  : " + baseUrl);
    }

    public String getUserAgent() {
        return UserAgent;
    }

}
