package org.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import solid.ren.skinlibrary.IDynamicNewView;
import solid.ren.skinlibrary.attr.base.DynamicAttr;
import solid.ren.skinlibrary.base.SkinBaseActivity;
import solid.ren.skinlibrary.loader.SkinInflaterFactory;

/**
 * Author: yuzzha
 * Date: 12/17/2019 5:21 PM
 * Description:
 * Remark:
 */
public abstract class BaseFragment extends RxFragment implements IDynamicNewView {

    protected RxAppCompatActivity mActivity;
    protected View mRootView;

    private Unbinder unbinder;

    private IDynamicNewView mIDynamicNewView;

    protected boolean isLazyLaod, hidden;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mIDynamicNewView = (IDynamicNewView) context;
        } catch (ClassCastException e) {
            mIDynamicNewView = null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof RxAppCompatActivity) {
            mActivity = (RxAppCompatActivity) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        initView();
        initData();
        setListener();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isLazyLaod && !hidden) {
            isLazyLaod = true;
            onLazyLaod();
        }
        this.hidden = hidden;
    }

    /**
     * 懒加载数据
     * 只加载一次
     */
    protected void onLazyLaod() {
    }

    /**
     * Gets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();


    /**
     * 初始化数据
     */
    protected void initData() {

    }


    /**
     * view与数据绑定
     */
    protected void initView() {

    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }

    @Override
    public void onDestroyView() {
        removeAllView(getView());
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected void initImmersionBar() {
        // ImmersionBar mImmersionBar = ImmersionBar.with(this);
        // ImmersionBar.with(this).keyboardEnable(true).init();
        //   mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    public final void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        if (mIDynamicNewView == null) {
            throw new RuntimeException("IDynamicNewView should be implements !");
        } else {
            mIDynamicNewView.dynamicAddView(view, pDAttrs);
        }
    }

    @Override
    public final void dynamicAddView(View view, String attrName, int attrValueResId) {
        mIDynamicNewView.dynamicAddView(view, attrName, attrValueResId);
    }

    @Override
    public final void dynamicAddFontView(TextView textView) {
        mIDynamicNewView.dynamicAddFontView(textView);
    }

    protected void removeAllView(View v) {
        if (v instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) v;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                removeAllView(viewGroup.getChildAt(i));
            }
            removeViewInSkinInflaterFactory(v);
        } else {
            removeViewInSkinInflaterFactory(v);
        }
    }

    private void removeViewInSkinInflaterFactory(View v) {
        if (getSkinInflaterFactory() != null) {
            //此方法用于Activity中Fragment销毁的时候，移除Fragment中的View
            getSkinInflaterFactory().removeSkinView(v);
        }
    }

    public final SkinInflaterFactory getSkinInflaterFactory() {
        if (getActivity() instanceof SkinBaseActivity) {
            return ((BaseActivity) getActivity()).getInflaterFactory();
        }
        return null;
    }
}

