package org.net.http.rxretrofit;

import android.app.Application;

import org.net.http.rxretrofit.api.BaseUrl;
import org.net.http.rxretrofit.core.RxHttpManager;
import org.utils.StringUtils;

public class NetSDK {


    private static Application application;
    private static boolean debug;
    private static String language;

    public static void init(Application app) {
        setApplication(app);
        setDebug(false);
        RxHttpManager.getInstance();
    }

    public static void init(Application app, String language, boolean debug) {
        setApplication(app);
        setLanguage(language);
        setDebug(debug);
    }

    public static Application getApplication() {
        return application;
    }

    public static void setBaseUrl(String baseUrl) {
        if (StringUtils.isNotEmpty(baseUrl)) {
           BaseUrl.getInstance().setBaseUrl(baseUrl);
        }
    }

    private static void setApplication(Application applications) {
        application = applications;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debugs) {
        debug = debugs;
        if (debugs) {
            BaseUrl.getInstance().initDebugInfo(application);
        }
    }

    public static String getLanguage() {
        return language;
    }

    public static void setLanguage(String language) {
        NetSDK.language = language;
    }
}
