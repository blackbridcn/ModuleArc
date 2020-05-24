package org.net.http.rxretrofit.listener;


import android.util.Log;

import com.application.BaseApplication;

import org.utils.RxToast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observable;

/**
 * Author: yuzzha
 * Date: 2019-07-12 10:59
 * Description: ${DESCRIPTION}
 * Remark:
 */
public abstract class RxHttpOnNextListener<T> {
    /**
     * 成功后回调方法
     *
     * @param t
     */
    public abstract void onNext(T t);

    /**
     * 緩存回調結果
     *
     * @param string
     */
    public void onCacheNext(String string) {

    }

    /**
     * 成功后的ober返回，扩展链接式调用
     *
     * @param observable
     */
    public void onNext(Observable observable) {

    }

    /**
     * 失败或者错误方法
     * 主动调用，更加灵活
     *
     * @param e
     */
    public void onError(Throwable e) {
        if (e instanceof ConnectException || e instanceof SocketTimeoutException) {
            RxToast.showDefultToast(BaseApplication.getContext(), "连接服务器异常，请稍后再试");
        } else {
            e.printStackTrace();
        }
    }

    /**
     * 取消回調
     */
    public void onCancel() {
        Log.e("TAG", "onCancel: ----------------> " );
    }
}
