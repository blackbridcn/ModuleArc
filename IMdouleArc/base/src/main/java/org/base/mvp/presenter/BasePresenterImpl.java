package org.base.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.base.mvp.view.BaseView;

public class BasePresenterImpl<T extends BaseView> implements BasePresenter<T> {

    protected T mPresenterView;

    @Override
    public void attachView(T t) {
        this.mPresenterView = t;
    }

    @Override
    public void detachView() {
        this.mPresenterView = null;
    }

    @Override
    public void makeToast(String msg) {
        if (mPresenterView instanceof Activity) {
            makeTost((Activity) mPresenterView, msg);
        } else if (mPresenterView instanceof Fragment) {
            makeTost(((Fragment) mPresenterView).getContext(), msg);
        }
    }

    @Override
    public void makeToast(int resId) {
        if (mPresenterView instanceof Activity) {
            makeTost((Activity) mPresenterView, ((Activity) mPresenterView).getResources().getString(resId));
        } else if (mPresenterView instanceof Fragment) {
            makeTost(((Fragment) mPresenterView).getContext(), (((Fragment) mPresenterView).getContext()).getResources().getString(resId));
        }
    }

    public void makeTost(Context mContext, String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    //用户手动点击开锁按钮时间
    long LAST_CLICK_TIME;
    //用户手动点击时间间隔
    long CLICK_ONPEN_INTERVAL = 3000;

    protected boolean isNotRepeatClick() {
        if ((System.currentTimeMillis() - LAST_CLICK_TIME) > CLICK_ONPEN_INTERVAL) {
            LAST_CLICK_TIME = System.currentTimeMillis();
            return true;
        } else {
           // LAST_CLICK_TIME = System.currentTimeMillis();
            // makeToast(UIUtils.getString(R.string.base_ble_operate_more));
            return false;
        }
    }


}
