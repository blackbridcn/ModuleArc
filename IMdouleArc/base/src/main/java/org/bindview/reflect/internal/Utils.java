package org.bindview.reflect.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.lang.reflect.Field;

/**
 * Author: yuzzha
 * Date: 2019/5/20 17:08
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class Utils {


    private static final TypedValue VALUE = new TypedValue();

    /**
     * 获取Drawable 资源
     *
     * @param context    Context
     * @param id         Drawable资源的id
     * @param tintAttrId
     * @return
     */
    @UiThread // Implicit synchronization for use of shared resource VALUE.
    public static Drawable getTintedDrawable(Context context,
                                             @DrawableRes int id, @AttrRes int tintAttrId) {
        boolean attributeFound = context.getTheme().resolveAttribute(tintAttrId, VALUE, true);
        if (!attributeFound) {
            throw new Resources.NotFoundException("Required tint color attribute with name "
                    + context.getResources().getResourceEntryName(tintAttrId)
                    + " and attribute ID "
                    + tintAttrId
                    + " was not found.");
        }
        Drawable drawable = ContextCompat.getDrawable(context, id);
        drawable = DrawableCompat.wrap(drawable.mutate());
        int color = ContextCompat.getColor(context, VALUE.resourceId);
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }

    /**
     * 执行contetnView 的 findViewById函数
     *
     * @param source contentView
     * @param id     Res Id
     * @param who    Tip 当id非法时的提示String
     * @return Widget View
     */
    public static View findRequiredView(View source, @IdRes int id, String who) {
        View view = source.findViewById(id);
        if (view != null) {
            return view;
        }
        String name = getResourceEntryName(source, id);
        throw new IllegalStateException("Required view '"
                + name
                + "' with ID "
                + id
                + " for "
                + who
                + " was not found. If this view is optional add '@Nullable' (fields) or '@Optional'"
                + " (methods) annotation.");
    }

    @UiThread // Implicit synchronization for use of shared resource VALUE.
    public static float getFloat(Context context, @DimenRes int id) {
        TypedValue value = VALUE;
        context.getResources().getValue(id, value, true);
        if (value.type == TypedValue.TYPE_FLOAT) {
            return value.getFloat();
        }
        throw new Resources.NotFoundException("Resource ID #0x" + Integer.toHexString(id)
                + " type #0x" + Integer.toHexString(value.type) + " is not valid");
    }


    /**
     * 获取widget在layout中的 id 的名称
     *
     * @param view source parentView
     * @param id   resint  id
     * @return id name
     */
    private static String getResourceEntryName(View view, @IdRes int id) {
        // View.isInEditMode()  判断View是否处于Edit状态
        if (view.isInEditMode()) {
            return "<unavailable while editing>";
        }
        //获取是widget在layout中的的 id 名字 例如：
        //  android:id="@+id/tv_1" 中的 tv_1
        return view.getContext().getResources().getResourceEntryName(id);
    }

    /**
     * Java 中的给 Fiele 设置值
     *
     * @param field  Field
     * @param target Field的持有者-->  class 对象
     * @param value  Field新的Value
     */
    public static void doSetFieldValueTask(Field field, Object target, @Nullable Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to assign " + value + " to " + field + " on " + target, e);
        }
    }

/*    private static <T extends View> List<T> findViews(View source, int[] ids, boolean isRequired,
                                                      String name, Class<? extends View> cls) {
        if (ids.length == 1 && ids[0] == View.NO_ID) {
            return singletonList((T) cls.cast(source));
        }

        String who = "method '" + name + "'";
        List<T> views = new ArrayList<>(ids.length);
        for (int id : ids) {
            if (isRequired) {
                views.add((T) Utils.findRequiredViewAsType(source, id, who, cls));
            } else {
                T view = (T) Utils.findOptionalViewAsType(source, id, who, cls);
                if (view != null) {
                    views.add(view);
                }
            }
        }
        return views;
    }*/

}
