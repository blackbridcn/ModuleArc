package org.skin.action;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.skin.SkinConfig;
import org.skin.attr.AttrFactory;
import org.skin.attr.DynamicAttr;
import org.skin.attr.SkinAttr;
import org.skin.core.SkinManager;
import org.skin.repositiory.SkinItem;
import org.skin.repositiory.TextViewRepository;
import org.skin.repositiory.ViewProducer;
import org.skin.utils.SkinListUtils;
import org.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * File: SkinInflaterFactory.java
 * Author: yuzhuzhang
 * Create: 2020/3/20 9:45 PM
 * Description: TODO 用来替代Activity的LayoutInflater 实习替换皮肤主题
 * -----------------------------------------------------------------
 * 2020/3/20 : Create SkinInflaterFactory.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class SkinInflaterFactory implements LayoutInflater.Factory2 {


    private AppCompatActivity mAppCompatActivity;

    /**
     * 存储那些有皮肤更改需求的View及其对应的属性的集合
     */
    private Map<View, SkinItem> mSkinItemMap = new HashMap<>();

    /**
     * Store the view item that need skin changing in the activity
     */
    private List<SkinItem> mSkinItems = new ArrayList<SkinItem>();

    public SkinInflaterFactory(AppCompatActivity appCompatActivity) {
        this.mAppCompatActivity = appCompatActivity;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {


        return null;
    }

    //先去判断这个当前将要View是否有更改皮肤的需求，如果没有我们就返回默认的实现。如果有，我们就自己去处理
    //来看看createView方法的实现
    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        boolean isSkinEnable = attrs.getAttributeBooleanValue(SkinConfig.NAMESPACE, SkinConfig.ATTR_SKIN_ENABLE, false);
        AppCompatDelegate delegate = this.mAppCompatActivity.getDelegate();
        View view = delegate.createView(parent, name, context, attrs);
        //这里是判断是不是textview 并且是否支持变化 ttf字体 如是添加到text仓库中
        if (view instanceof TextView && SkinConfig.isCanChangeFont()) {
            TextViewRepository.add(this.mAppCompatActivity, (TextView) view);
        }
        if (isSkinEnable && SkinConfig.isGlobalSkinApply()) {
            if (view == null) {
                view = ViewProducer.createViewFromTag(context, name, attrs);
            }
            if (view == null) {
                return null;
            }
        }
        parseSkinAttr(context, attrs, view);

        return null;
    }


    private void parseSkinAttr(Context context, AttributeSet attrs, View view) {
        List<SkinAttr> viewAttrs = new ArrayList<>();//用来存储View 中在变换skin时需要变换的属性
        LogUtils.i("viewName:" + view.getClass().getSimpleName());
        //遍历View的所用属性
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);//属性名
            String attrValue = attrs.getAttributeValue(i);//属性值
            LogUtils.i("    AttributeName:" + attrName + "|attrValue:" + attrValue);
            //region  style
            //style theme
            if ("style".equals(attrName)) {
                int[] skinAttrs = new int[]{android.R.attr.textColor, android.R.attr.background};
                TypedArray a = context.getTheme().obtainStyledAttributes(attrs, skinAttrs, 0, 0);
                int textColorId = a.getResourceId(0, -1);
                int backgroundId = a.getResourceId(1, -1);
                if (textColorId != -1) {
                    String entryName = context.getResources().getResourceEntryName(textColorId);
                    String typeName = context.getResources().getResourceTypeName(textColorId);
                    SkinAttr skinAttr = AttrFactory.get("textColor", textColorId, entryName, typeName);

                    LogUtils.w("    textColor in style is supported:" + "\n" +
                            "    resource id:" + textColorId + "\n" +
                            "    attrName:" + attrName + "\n" +
                            "    attrValue:" + attrValue + "\n" +
                            "    entryName:" + entryName + "\n" +
                            "    typeName:" + typeName);

                    if (skinAttr != null) {
                        viewAttrs.add(skinAttr);
                    }
                }
                if (backgroundId != -1) {
                    String entryName = context.getResources().getResourceEntryName(backgroundId);
                    String typeName = context.getResources().getResourceTypeName(backgroundId);
                    SkinAttr skinAttr = AttrFactory.get("background", backgroundId, entryName, typeName);
                    LogUtils.w("    background in style is supported:" + "\n" +
                            "    resource id:" + backgroundId + "\n" +
                            "    attrName:" + attrName + "\n" +
                            "    attrValue:" + attrValue + "\n" +
                            "    entryName:" + entryName + "\n" +
                            "    typeName:" + typeName);
                    if (skinAttr != null) {
                        viewAttrs.add(skinAttr);
                    }

                }
                a.recycle();
                continue;
            }
            //endregion
            //if attrValue is reference，eg:@color/red
            if (AttrFactory.isSupportedAttr(attrName) && attrValue.startsWith("@")) {
                try {
                    //resource id
                    int id = Integer.parseInt(attrValue.substring(1));
                    if (id == 0) {
                        continue;
                    }
                    //entryName，eg:text_color_selector
                    String entryName = context.getResources().getResourceEntryName(id);
                    //typeName，eg:color、drawable
                    String typeName = context.getResources().getResourceTypeName(id);
                    SkinAttr mSkinAttr = AttrFactory.get(attrName, id, entryName, typeName);

                    LogUtils.w("    " + attrName + " is supported:" + "\n" +
                            "    resource id:" + id + "\n" +
                            "    attrName:" + attrName + "\n" +
                            "    attrValue:" + attrValue + "\n" +
                            "    entryName:" + entryName + "\n" +
                            "    typeName:" + typeName
                    );

                    if (mSkinAttr != null) {
                        viewAttrs.add(mSkinAttr);
                    }
                } catch (NumberFormatException e) {
                    LogUtils.e(e.toString());
                }
            }
        }
        if (!SkinListUtils.isEmpty(viewAttrs)) {
            SkinItem skinItem = new SkinItem();
            skinItem.view = view;
            skinItem.attrs = viewAttrs;
            mSkinItemMap.put(skinItem.view, skinItem);
            if (SkinManager.getInstance().isExternalSkin() ||
                    SkinManager.getInstance().isNightMode()) {//如果当前皮肤来自于外部或者是处于夜间模式
                skinItem.apply();
            }
        }
    }

    public void applySkin() {
        if (!SkinListUtils.isEmpty(mSkinItems)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mSkinItems.forEach(skinItem -> {
                    if (skinItem != null) {
                        skinItem.apply();
                    }
                });
            } else {
                for (SkinItem mSkinItem : mSkinItems) {
                    if (mSkinItem != null) {
                        mSkinItem.apply();
                    }
                }
            }
        }
    }

    public void clean() {
        if (!SkinListUtils.isEmpty(mSkinItems)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mSkinItems.forEach(skinItem -> {
                    if (skinItem != null) {
                        skinItem.clean();
                    }
                });
            } else {
                for (SkinItem mSkinItem : mSkinItems) {
                    if (mSkinItem != null) {
                        mSkinItem.clean();
                    }
                }
            }
        }
    }

    public void dynamicAddSkinEnableView(Context context, View view, List<DynamicAttr> pDAttrs) {
        List<SkinAttr> viewAttrs = new ArrayList<SkinAttr>();
        SkinItem skinItem = new SkinItem();
        skinItem.view = view;

        for (DynamicAttr dAttr : pDAttrs) {
            int id = dAttr.refResId;
            String entryName = context.getResources().getResourceEntryName(id);
            String typeName = context.getResources().getResourceTypeName(id);
            SkinAttr mSkinAttr = AttrFactory.get(dAttr.attrName, id, entryName, typeName);
            viewAttrs.add(mSkinAttr);
        }

        skinItem.attrs = viewAttrs;
        addSkinView(skinItem);
    }

    public void dynamicAddSkinEnableView(Context context, View view, String attrName, int attrValueResId) {
        int id = attrValueResId;
        String entryName = context.getResources().getResourceEntryName(id);
        String typeName = context.getResources().getResourceTypeName(id);
        SkinAttr skinAttr = AttrFactory.get(attrName, id, entryName, typeName);
        SkinItem skinItem = new SkinItem();
        skinItem.view = view;
        List<SkinAttr> arrayList = new ArrayList<SkinAttr>();
        arrayList.add(skinAttr);
        skinItem.attrs = arrayList;
        addSkinView(skinItem);
    }

    public void addSkinView(SkinItem mSkinItem) {
        mSkinItems.add(mSkinItem);
    }


    public void removeSkinView(View view) {
        //SkinL.i(TAG, "removeSkinView:" + view);
        SkinItem skinItem = mSkinItemMap.remove(view);
        if (skinItem != null) {
         //   SkinL.w(TAG, "removeSkinView from mSkinItemMap:" + skinItem.view);
        }
        if (SkinConfig.isCanChangeFont() && view instanceof TextView) {
         //   SkinL.e(TAG, "removeSkinView from TextViewRepository:" + view);
            TextViewRepository.remove(mAppCompatActivity, (TextView) view);
        }
    }


}
