package org.net.http.rxretrofit.core;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.net.http.rxretrofit.NetSDK;
import org.net.http.rxretrofit.api.BaseUrl;
import org.net.http.rxretrofit.api.RxBaseApi;
import org.net.http.rxretrofit.certs.TrustAllCerts;
import org.net.http.rxretrofit.cookie.CookieJarImpl;
import org.net.http.rxretrofit.exception.RetryWhenNetworkException;
import org.net.http.rxretrofit.interceptor.HttpLogInterceptor;
import org.net.http.rxretrofit.listener.RxHttpOnNextListener;
import org.net.http.rxretrofit.subscribers.ProgressSubscriber;
import org.utils.LogUtils;

import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: yuzzha
 * Date: 2019-07-12 11:07
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class RxHttpManager {

    private volatile static RxHttpManager INSTANCE;
    private static final int DEFAULT_NET_CONN_TIMEOUT = 6;
    private OkHttpClient client;
    private Retrofit.Builder build;

    //构造方法私有
    private RxHttpManager() {
    }

    //获取单例
    public static RxHttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (RxHttpManager.class) {
                if (INSTANCE == null)
                    INSTANCE = new RxHttpManager();
            }
        }
        return INSTANCE;
    }


    public void initOkhttpClient(Interceptor cInterceptor) {
        if (client != null) return;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //Cookie 管理
        builder.cookieJar(CookieJarImpl.getInstance());
        //超时设置
        builder.connectTimeout(DEFAULT_NET_CONN_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(cInterceptor);//peake 登录拦截
        if (NetSDK.isDebug()) {
            builder.addNetworkInterceptor(getHttpLoggingInterceptor());
            builder.addInterceptor(getHeaderInterceptor());
        }
        builder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory());
        builder.hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier());
        client = builder.build();

        /*创建retrofit对象*/
        build = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }


    public void doHttpRequest(RxBaseApi basePar) {
        if (client == null)
            initOkhttpClient(getLoginInterceptor());
        Retrofit retrofit = build.client(client)
                .baseUrl(basePar.getBaseUrl())
                .build();
        /*rx处理*/
        ProgressSubscriber subscriber = new ProgressSubscriber(basePar);
        /**/
        Observable observable = basePar.getObservable(retrofit)
                .retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(), basePar.getRetryDelay(), basePar.getRetryIncreaseDelay()))
                /*生命周期bding*/
                .compose(basePar.getRxAppCompatActivity().bindUntilEvent(ActivityEvent.PAUSE))
                //请求线程
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果处理*/
                .map(basePar);

        /*链接式对象返回*/
        SoftReference<RxHttpOnNextListener> httpOnNextListener = basePar.getListener();
        if (httpOnNextListener != null && httpOnNextListener.get() != null) {
            httpOnNextListener.get().onNext(observable);
        }
        /*数据回调*/
        observable.subscribe(subscriber);
    }

    public void doBackHttpRequestTask(RxBaseApi basePar) {
        if (client == null)
            initOkhttpClient(getLoginInterceptor());
        /*创建retrofit对象*/
        Retrofit retrofit = build.client(client)
                .baseUrl(basePar.getBaseUrl())
                .build();
        /*rx处理*/
        ProgressSubscriber subscriber = new ProgressSubscriber(basePar);
        /**/
        Observable observable = basePar.getObservable(retrofit)
                .retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(), basePar.getRetryDelay(), basePar.getRetryIncreaseDelay()))
                //请求线程
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(Schedulers.io())
                /*结果处理*/
                .map(basePar);

        /*链接式对象返回*/
        SoftReference<RxHttpOnNextListener> httpOnNextListener = basePar.getListener();
        if (httpOnNextListener != null && httpOnNextListener.get() != null) {
            httpOnNextListener.get().onNext(observable);
        }
        /*数据回调*/
        observable.subscribe(subscriber);
    }

    /**
     * 日志输出
     * 自行判定是否添加
     *
     * @return
     */
    private HttpLogInterceptor getHttpLoggingInterceptor() {
        //日志显示级别
        HttpLogInterceptor.Level level = HttpLogInterceptor.Level.BODY;
        //新建log拦截器
        HttpLogInterceptor loggingInterceptor = new HttpLogInterceptor(message -> {
            if (true)
                Log.e("HTTP", "Retrofit OkHttp Log :" + message);
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

    private Interceptor getHeaderInterceptor() {
        return chain -> {
            //1.0 Get  Request
            Request request = chain.request();
            //TODO add header
            request.header("User-Agent:" + BaseUrl.getInstance().getUserAgent());
            //2.0 use chain task request
            Response response = chain.proceed(request);
            //3.0 return Response
            return response;
        };
    }

    private Interceptor getLoginInterceptor() {
        try {
            LogUtils.d("TAG", "getLoginInterceptor: --------> " );
            Class clazz = Class.forName("org.net.interceptor.LoginInterceptor");
            Object instance = clazz.newInstance();
            if (instance instanceof Interceptor) {
                return (Interceptor) instance;
            }
        } catch (ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        LogUtils.i("Interceptor ", "App Login Interceptor Exception : NETSDK 初始化时设置Http登录超时拦截器失败");
        return (Interceptor) chain -> {
            return chain.proceed(chain.request());
        };
    }

}
