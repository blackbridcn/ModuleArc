package org.skin.view;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.skin.SkinConfig;
import org.skin.action.IDynamicNewView;
import org.skin.action.ISkinUpdate;
import org.skin.action.SkinInflaterFactory;
import org.skin.attr.DynamicAttr;
import org.skin.core.SkinManager;
import org.skin.utils.SkinResourcesUtils;

import java.util.List;

/**
 * Author: yuzzha
 * Date: 3/24/2020 2:39 PM
 * Description:
 * Remark:
 */
public class BaseSkinActivity extends AppCompatActivity implements ISkinUpdate, IDynamicNewView {

    private SkinInflaterFactory mSkinInflaterFactory;
    /**
     * Whether response to skin changing after create
     */
    private boolean isResponseOnSkinChanging = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSkinInflaterFactory = new SkinInflaterFactory(this);
        //这里是使用重写的的LayoutInflaterFactory 替代Activity中默认的Factory
        getLayoutInflater().setFactory2(mSkinInflaterFactory);

        // mSkinInflaterFactory = new SkinInflaterFactory(this);
        // LayoutInflaterCompat.setFactory2(getLayoutInflater(), mSkinInflaterFactory);
        super.onCreate(savedInstanceState);
        changeStatusColor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SkinManager.getInstance().attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().detach(this);
        mSkinInflaterFactory.clean();
    }

    @Override
    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {

    }

    @Override
    public void dynamicAddView(View view, String attrName, int attrValueResId) {

    }

    @Override
    public void dynamicAddFontView(TextView textView) {

    }

    @Override
    public void onThemeUpdate() {
        if (!isResponseOnSkinChanging) {
            return;
        }
        mSkinInflaterFactory.applySkin();
        changeStatusColor();
    }


    public SkinInflaterFactory getInflaterFactory() {
        return mSkinInflaterFactory;
    }

    public void changeStatusColor() {
        if (!SkinConfig.isCanChangeStatusColor()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = SkinResourcesUtils.getColorPrimaryDark();
            if (color != -1) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(SkinResourcesUtils.getColorPrimaryDark());
            }
        }
    }

    /**
     * dynamic add a skin view
     *
     * @param view
     * @param attrName
     * @param attrValueResId
     */
    protected void dynamicAddSkinEnableView(View view, String attrName, int attrValueResId) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, attrName, attrValueResId);
    }

    protected void dynamicAddSkinEnableView(View view, List<DynamicAttr> pDAttrs) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }

    final protected void enableResponseOnSkinChanging(boolean enable) {
        isResponseOnSkinChanging = enable;
    }
}
