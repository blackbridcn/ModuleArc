package org.net.http.rxretrofit.subscribers;

import android.content.Context;

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import org.dialog.alert.AsynTaskDialog;
import org.net.http.rxretrofit.api.RxBaseApi;
import org.net.http.rxretrofit.listener.RxHttpOnNextListener;

import java.lang.ref.SoftReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Author: yuzzha
 * Date: 2019-07-12 11:10
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class ProgressSubscriber<T> implements Observer<T> {
    /*是否弹框*/
    private boolean showPorgress = true;
    /* 软引用回调接口*/
    private SoftReference<RxHttpOnNextListener> mSubscriberOnNextListener;
    /*软引用反正内存泄露*/
    private SoftReference<RxAppCompatActivity> mActivity;
    /*加载框可自己定义*/
    private AsynTaskDialog mDialog;


    public ProgressSubscriber(RxBaseApi api) {
        this.mSubscriberOnNextListener = api.getListener();
        this.mActivity = new SoftReference<>(api.getRxAppCompatActivity());
        setShowPorgress(api.isShowProgress());
        if (api.isShowProgress()) {
            initProgressDialog(api.isCancel());
        }
    }


    /**
     * 是否需要弹框设置
     *
     * @param showPorgress
     */
    public void setShowPorgress(boolean showPorgress) {
        this.showPorgress = showPorgress;
    }

    private void initProgressDialog(boolean cancel) {
        Context context = mActivity.get();
        if (mDialog == null && context != null) {
            mDialog = new AsynTaskDialog(context);
            mDialog.setCancelable(cancel);
            if (cancel) {
                mDialog.setOnCancelListener((mDialogInterface) -> {
                    if (mSubscriberOnNextListener.get() != null) {
                        mSubscriberOnNextListener.get().onCancel();
                    }
                    onCancelProgress();
                });
            }
        }
    }

    private Disposable onDisposable;

    @Override
    public void onSubscribe(Disposable d) {
        this.onDisposable = d;
        showProgressDialog();
    }


    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onNext(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        OnErrorTodo(e);
    }

    /*错误统一处理*/
    private void OnErrorTodo(Throwable e) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onError(e);
        }
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
    }

    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        if (!isShowPorgress()) return;
        Context context = mActivity.get();
        if (mDialog == null || context == null) return;
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }


    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        if (!isShowPorgress()) return;
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelProgress() {
        if (!this.onDisposable.isDisposed()) {
            this.onDisposable.dispose();
        }
    }


    public boolean isShowPorgress() {
        return showPorgress;
    }


}
