package org.skin.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.ColorRes;
import androidx.annotation.RestrictTo;
import androidx.core.content.ContextCompat;

import org.skin.SkinConfig;
import org.skin.action.ISkinUpdate;
import org.skin.repositiory.TextViewRepository;
import org.skin.utils.ResourcesCompat;
import org.skin.utils.SkinFileUtils;
import org.skin.utils.TypefaceUtils;
import org.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * File: SkinManager.java
 * Author: yuzhuzhang
 * Create: 2020/3/21 4:26 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/21 : Create SkinManager.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class SkinManager implements ISkinLoder {

    private static volatile SkinManager mInstance;

    /**
     * skin package name
     */
    private String skinPackageName;
    private Context mContext;
    private Resources mResources;
    private boolean isDefaultSkin = false;

    private List<ISkinUpdate> mSkinObservers;


    private SkinManager() {

    }

    public static SkinManager getInstance() {
        if (mInstance == null) {
            synchronized (SkinManager.class) {
                if (mInstance == null) {
                    mInstance = new SkinManager();
                }
            }
        }
        return mInstance;
    }


    //初始化
    public void initSkinSDK(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        //初始化typeface 即textview的ttf 的路径
        TypefaceUtils.CURRENT_TYPEFACE = TypefaceUtils.getTypeface(mContext);
        setUpSkinFile(mContext);
        //黑夜模式
        boolean inNightMode = SkinConfig.isInNightMode(mContext);
        if (inNightMode) {
            nightMode();
        } else {
            String skin = SkinConfig.getCustomSkinPath(mContext);
            if (SkinConfig.isDefaultSkin(mContext)) {
                return;
            }
            loadSkin(skin, null);
        }
    }

    //初始化在指定目录下的skin文件路径  这里默认是assert目录下skin 文件
    private void setUpSkinFile(Context context) {
        try {
            String[] skinFiles = context.getAssets().list(SkinConfig.SKIN_DIR_NAME);
            for (String fileName : skinFiles) {
                File file = new File(SkinFileUtils.getSkinDir(context), fileName);
                if (!file.exists()) {
                    SkinFileUtils.copySkinAssetsToDir(context, fileName, SkinFileUtils.getSkinDir(context));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //这里是从assert中加载skin皮肤包
    @SuppressLint("StaticFieldLeak")
    public void loadSkin(String skinName, final SkinLoaderListener callback) {

        new AsyncTask<String, Void, Resources>() {

            @Override
            protected void onPreExecute() {
                if (callback != null) {
                    callback.onStart();
                }
            }

            @Override
            protected Resources doInBackground(String... params) {
                try {
                    if (params.length == 1) {
                        String skinPkgPath = SkinFileUtils.getSkinDir(mContext) + File.separator + params[0];
                        LogUtils.i("skinPackagePath:" + skinPkgPath);
                        File file = new File(skinPkgPath);
                        if (!file.exists()) {
                            return null;
                        }
                        PackageManager mPm = mContext.getPackageManager();
                        PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPkgPath, PackageManager.GET_ACTIVITIES);
                        skinPackageName = mInfo.packageName;
                        AssetManager assetManager = AssetManager.class.newInstance();
                        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                        addAssetPath.invoke(assetManager, skinPkgPath);


                        Resources superRes = mContext.getResources();
                        Resources skinResource = ResourcesCompat.getResources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
                        SkinConfig.saveSkinPath(mContext, params[0]);

                        isDefaultSkin = false;
                        return skinResource;
                    }
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Resources result) {
                mResources = result;

                if (mResources != null) {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                    SkinConfig.setNightMode(mContext, false);
                    notifySkinUpdate();
                } else {
                    isDefaultSkin = true;
                    if (callback != null) {
                        callback.onFailed("没有获取到资源");
                    }
                }
            }

        }.execute(skinName);
    }

    @Override
    public void attach(ISkinUpdate observer) {
        if (mSkinObservers == null) {
            mSkinObservers = new ArrayList<>();
        }
        if (!mSkinObservers.contains(observer)) {
            mSkinObservers.add(observer);
        }
    }

    @Override
    public void detach(ISkinUpdate observer) {
        if (mSkinObservers != null && mSkinObservers.contains(observer)) {
            mSkinObservers.remove(observer);
        }
    }


    @Override
    public void notifySkinUpdate() {
        if (mSkinObservers == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mSkinObservers.forEach(iSkinUpdate -> iSkinUpdate.onThemeUpdate());
        } else {
            for (ISkinUpdate mSkinObserver : mSkinObservers) {
                mSkinObserver.onThemeUpdate();
            }
        }
    }

    public boolean isExternalSkin() {
        return !isDefaultSkin && mResources != null;
    }

    public boolean isNightMode() {
        return SkinConfig.isInNightMode(mContext);
    }

    public void nightMode() {
        if (!isDefaultSkin) {
            restoreDefaultTheme();
        }
        SkinConfig.setNightMode(mContext, true);
        notifySkinUpdate();
    }

    public void loadFont(String fontName) {
        Typeface tf = TypefaceUtils.createTypeface(mContext, fontName);
        TextViewRepository.applyFont(tf);
    }


    //恢复到default的 Skin
    public void restoreDefaultTheme() {
        SkinConfig.saveSkinPath(mContext, SkinConfig.DEFAULT_SKIN);
        isDefaultSkin = true;
        SkinConfig.setNightMode(mContext, false);
        mResources = mContext.getResources();
        skinPackageName = mContext.getPackageName();
        notifySkinUpdate();
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public Resources getResources() {
        return mResources;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public int getColor(@ColorRes int colorId) {
        int orginColor = ContextCompat.getColor(mContext, colorId);
        if (mResources == null || isDefaultSkin) {
            return orginColor;
        }
        //通过id查询res文件的name
        String resName = mResources.getResourceEntryName(colorId);
        //通过资源name 获取对应的skin 的packageName下对应的color
        int identifier = mResources.getIdentifier(resName, "color", skinPackageName);
        int showColor;
        if (identifier == 0) {
            showColor = orginColor;
        } else {
            showColor = mResources.getColor(identifier);
        }
        return showColor;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public int getNightColor(int resId) {
        String resName = mResources.getResourceEntryName(resId);
        String resNameNight = resName + "_night";
        int nightResId = mResources.getIdentifier(resNameNight, "color", skinPackageName);
        if (nightResId == 0) {
            return ContextCompat.getColor(mContext, resId);
        } else {
            return ContextCompat.getColor(mContext, nightResId);
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public ColorStateList getNightColorStateList(int resId) {
        String resName = mResources.getResourceEntryName(resId);
        String resNameNight = resName + "_night";
        int nightResId = mResources.getIdentifier(resNameNight, "color", skinPackageName);
        if (nightResId == 0) {
            return ContextCompat.getColorStateList(mContext, resId);
        } else {
            return ContextCompat.getColorStateList(mContext, nightResId);
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public Drawable getNightDrawable(String resName) {
        String resNameNight = resName + "_night";

        int nightResId = mResources.getIdentifier(resNameNight, "drawable", skinPackageName);
        if (nightResId == 0) {
            nightResId = mResources.getIdentifier(resNameNight, "mipmap", skinPackageName);
        }
        Drawable color;
        if (nightResId == 0) {
            int resId = mResources.getIdentifier(resName, "drawable", skinPackageName);
            if (resId == 0) {
                resId = mResources.getIdentifier(resName, "mipmap", skinPackageName);
            }
            color = mResources.getDrawable(resId);
        } else {
            color = mResources.getDrawable(nightResId);
        }
        return color;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public Drawable getDrawable(int resId, String dir) {
        Drawable originDrawable = ContextCompat.getDrawable(mContext, resId);
        if (mResources == null || isDefaultSkin) {
            return originDrawable;
        }
        String resName = mResources.getResourceEntryName(resId);
        int trueResId = mResources.getIdentifier(resName, dir, skinPackageName);
        Drawable trueDrawable;
        if (trueResId == 0) {
            trueDrawable = originDrawable;
        } else {
            if (Build.VERSION.SDK_INT < 22) {
                trueDrawable = mResources.getDrawable(trueResId);
            } else {
                trueDrawable = mResources.getDrawable(trueResId, null);
            }
        }
        return trueDrawable;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public Drawable getDrawable(int resId) {
        Drawable originDrawable = ContextCompat.getDrawable(mContext, resId);
        if (mResources == null || isDefaultSkin) {
            return originDrawable;
        }
        String resName = mContext.getResources().getResourceEntryName(resId);
        int trueResId = mResources.getIdentifier(resName, "drawable", skinPackageName);
        Drawable trueDrawable;
        if (trueResId == 0) {
            trueResId = mResources.getIdentifier(resName, "mipmap", skinPackageName);
        }
        if (trueResId == 0) {
            trueDrawable = originDrawable;
        } else {
            if (Build.VERSION.SDK_INT < 22) {
                trueDrawable = mResources.getDrawable(trueResId);
            } else {
                trueDrawable = mResources.getDrawable(trueResId, null);
            }
        }
        return trueDrawable;
    }

    public String getCurSkinPackageName() {
        return skinPackageName;
    }

    /**
     * 加载指定资源颜色drawable,转化为ColorStateList，保证selector类型的Color也能被转换。
     * 无皮肤包资源返回默认主题颜色
     * author:pinotao
     *
     * @param resId resources id
     * @return ColorStateList
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public ColorStateList getColorStateList(int resId) {
        boolean isExternalSkin = true;
        if (mResources == null || isDefaultSkin) {
            isExternalSkin = false;
        }

        String resName = mContext.getResources().getResourceEntryName(resId);
        if (isExternalSkin) {
            int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
            ColorStateList trueColorList;
            if (trueResId == 0) { // 如果皮肤包没有复写该资源，但是需要判断是否是ColorStateList

                return ContextCompat.getColorStateList(mContext, resId);
            } else {
                trueColorList = mResources.getColorStateList(trueResId);
                return trueColorList;
            }
        } else {
            return ContextCompat.getColorStateList(mContext, resId);
        }
    }

    public interface SkinLoaderListener {
        void onStart();

        void onSuccess();

        void onFailed(String errMsg);

        /**
         * called when from network load skin
         *
         * @param progress download progress
         */
        void onProgress(int progress);
    }
}
