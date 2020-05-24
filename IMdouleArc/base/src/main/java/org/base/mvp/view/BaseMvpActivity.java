package org.base.mvp.view;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.base.BaseActivity;
import org.base.mvp.presenter.BasePresenter;

public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity implements BaseView {

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initInjector();
        mPresenter.attachView(this);
        initData();
    }

    protected abstract T initInjector();
    /**
     * 初始化数据
     */
    protected void initData() {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
