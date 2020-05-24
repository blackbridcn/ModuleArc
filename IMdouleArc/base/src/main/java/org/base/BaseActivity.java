package org.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

import com.base.R;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import org.immersionbar.ImmersionBar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import solid.ren.skinlibrary.IDynamicNewView;
import solid.ren.skinlibrary.ISkinUpdate;
import solid.ren.skinlibrary.attr.base.DynamicAttr;
import solid.ren.skinlibrary.loader.SkinInflaterFactory;
import solid.ren.skinlibrary.loader.SkinManager;

public abstract class BaseActivity extends RxAppCompatActivity implements ISkinUpdate, IDynamicNewView {

    //private NetworkConnectChangedReceiver netWorkChangReceiver;
    protected Unbinder unbinder;
    private SkinInflaterFactory mSkinInflaterFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSkinInflaterFactory = new SkinInflaterFactory(this);
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), mSkinInflaterFactory);
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        setContentView(initLayout());
        unbinder = ButterKnife.bind(this);
        //changeStatusColor();
        //初始化沉浸式
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        SkinManager.getInstance().attach(this);
    }

    @Override
    public void onThemeUpdate() {
        mSkinInflaterFactory.applySkin();
        //changeStatusColor();
    }

    public SkinInflaterFactory getInflaterFactory() {
        return mSkinInflaterFactory;
    }

   /* public void changeStatusColor() {
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
    }*/

    @Override
    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }

    @Override
    public void dynamicAddView(View view, String attrName, int attrValueResId) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, attrName, attrValueResId);
    }

    @Override
    public void dynamicAddFontView(TextView textView) {
        mSkinInflaterFactory.dynamicAddFontEnableView(this, textView);
    }

    protected <T extends View> T $(int id) {
        return (T) super.findViewById(id);
    }

    /**
     * 注册广播
     */
    protected void registerReceiver() {
        //注册网络状态监听广播 7.
        //  netWorkChangReceiver = new NetworkConnectChangedReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //   registerReceiver(netWorkChangReceiver, filter);
    }

    protected void unRegisterReceiver() {
        // if (netWorkChangReceiver != null)
        //      unregisterReceiver(netWorkChangReceiver);
    }

    //判断是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void initImmersionBar() {
        View toolbar = initTitleBar();
        if (toolbar != null) {
            ImmersionBar.with(this).titleBar(toolbar)
                    .keyboardEnable(true)
                    .init();
        } else {
            ImmersionBar.with(this).init();
        }
    }

    protected View initTitleBar() {
        return null;
    }


    /**
     * 获取状态栏的高度
     *
     * @return
     */
    protected int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 布局ID
     *
     * @return
     */
    protected abstract int initLayout();

    /**
     * 初始化view
     */
    protected abstract void initView();

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        fixInputMethodManagerLeak(this);
        unRegisterReceiver();
        SkinManager.getInstance().detach(this);
        mSkinInflaterFactory.clean();
        unbinder.unbind();
        unbinder = null;
    }

    /**
     * 解决InputMethodManager内存泄露现象
     */
    private static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f;
        Object obj_get;
        for (String param : arr) {
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get
                            .getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        /*if (QLog.isColorLevel()) {
                            QLog.d(ReflecterHelper.class.getSimpleName(), QLog.CLR, "fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext()+" dest_context=" + destContext);
                        }*/
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
