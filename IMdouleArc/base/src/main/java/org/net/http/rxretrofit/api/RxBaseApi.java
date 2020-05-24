package org.net.http.rxretrofit.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.base.BaseJson;
import com.base.BaseNetErrorCode;
import com.base.param.BaseRespJson;
import com.base.param.list.BaseRespList;
import com.base.param.obj.BaseRespObj;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import org.net.http.rxretrofit.listener.RxHttpOnNextListener;
import org.net.http.rxretrofit.utils.Utils;
import org.utils.StringUtils;

import java.io.IOException;
import java.lang.ref.SoftReference;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Author: yuzzha
 * Date: 2019-07-12 10:56
 * Description: ${DESCRIPTION}
 * Remark:
 */
public abstract class RxBaseApi<T> implements Function<ResponseBody, T> {
    /*超时时间-默认6秒*/
    private int connectionTime = 6;
    /*基础url*/
    private String baseUrl;
    //rx生命周期管理
    private SoftReference<RxAppCompatActivity> rxAppCompatActivity;
    /*回调*/
    private SoftReference<RxHttpOnNextListener> listener;

    /*是否能取消加载框*/
    private boolean cancel = true;
    /*是否显示加载框*/
    private boolean showProgress = true;

    /* 失败后retry次数*/
    private int retryCount = 1;
    /*失败后retry延迟*/
    private long retryDelay = 100;
    /*失败后retry叠加延迟*/
    private long retryIncreaseDelay = 10;

    /*是否需要缓存处理*/
    private boolean cache;

    public RxBaseApi(RxHttpOnNextListener listener, RxAppCompatActivity rxAppCompatActivity) {
        setListener(listener);
        setRxAppCompatActivity(rxAppCompatActivity);
        setShowProgress(true);
        setCache(false);
        setCancel(true);
        //  setCookieNetWorkTime(60);
        // setCookieNoNetWorkTime(24*60*60);
    }


    /**
     * 设置参数
     *
     * @param retrofit
     * @return
     */
    public abstract Observable getObservable(Retrofit retrofit);

    /**
     * 解密服务端返回的 参数
     * 备注：只解密原始参数
     *
     * @param resualtPar
     * @return
     */
 /*   protected String deceryed(String resualtPar) {
        return MoToolingEncryptClient.decryptionData(resualtPar);
    }*/

    /**
     * 将参数进行加密
     *
     * @param
     * @return
     */
   /* protected String encryed(Map<String, Object> params) {
        return PeakeEncrypt.getRequestData(params);
    }*/

    protected BaseJson convertJson(ResponseBody response) {
        String string = null;
        BaseJson baseJson = null;
        try {
            string = response.string();
            string.length();
            if (!Utils.isEmpty(string)) {
                String errList = "\"resultData\":{\"list\":\"\"}";
                String okList = "\"resultData\":{\"list\":[]}";
                string = string.replace(errList, okList);
            }
            baseJson = JSON.parseObject(string, BaseJson.class);
        } catch (IOException e) {
            e.printStackTrace();
            if (baseJson == null)
                baseJson = new BaseJson();
            baseJson.setResultMsg(BaseNetErrorCode.OTHER_ERROR.getDesc());
        }
        return baseJson;
    }


    protected BaseRespJson<BaseRespList<T>> convertJsonToList(Response response) throws Exception {
        String string = response.body().string();
        if (!StringUtils.isEmpty(string)) {
            String errList = "\"resultData\":{\"list\":\"\"}";
            String okList = "\"resultData\":{\"list\":[]}";
            string = string.replace(errList, okList);
        }
        return JSON.parseObject(string, new TypeReference<BaseRespJson<BaseRespList<T>>>() {
        });
    }

    protected BaseRespJson<BaseRespObj<T>> convertJsonToObj(Response response) throws Exception {
        String string = response.body().string();
        if (!StringUtils.isEmpty(string)) {
            String errList = "\"resultData\":{\"list\":\"\"}";
            String okList = "\"resultData\":{\"list\":[]}";
            string = string.replace(errList, okList);
        }
        return JSON.parseObject(string, new TypeReference<BaseRespJson<BaseRespObj<T>>>() {
        });
    }

    public int getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(int connectionTime) {
        this.connectionTime = connectionTime;
    }

    public String getBaseUrl() {
        if (Utils.isEmpty(this.baseUrl)) {
            return BaseUrl.getInstance().getBaseUrl();
        }
        return baseUrl;
    }

    public void setBaseUrl(String baseUrls) {
        this.baseUrl = baseUrls;
    }

    public SoftReference<RxHttpOnNextListener> getListener() {
        return listener;
    }

    public void setListener(RxHttpOnNextListener listener) {
        this.listener = new SoftReference(listener);
    }

    public void setRxAppCompatActivity(RxAppCompatActivity rxAppCompatActivity) {
        if (rxAppCompatActivity != null)
            this.rxAppCompatActivity = new SoftReference(rxAppCompatActivity);
    }

    /**
     * 获取当前rx生命周期
     *
     * @return
     */
    public RxAppCompatActivity getRxAppCompatActivity() {
        if (rxAppCompatActivity != null)
            return rxAppCompatActivity.get();
        return null;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public long getRetryDelay() {
        return retryDelay;
    }

    public void setRetryDelay(long retryDelay) {
        this.retryDelay = retryDelay;
    }

    public long getRetryIncreaseDelay() {
        return retryIncreaseDelay;
    }

    public void setRetryIncreaseDelay(long retryIncreaseDelay) {
        this.retryIncreaseDelay = retryIncreaseDelay;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }
}
