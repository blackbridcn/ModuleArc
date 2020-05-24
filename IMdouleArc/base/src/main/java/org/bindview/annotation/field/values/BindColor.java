package org.bindview.annotation.field.values;

import androidx.annotation.ColorRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: yuzzha
 * Date: 2019/5/20 18:12
 * Description: Color颜色的绑定注解
 * Remark: 参照了Butterknife中的BindColor
 * <p>
 * 使用方式
 * BindColor(R.color.background_green) int green;
 * BindColor(R.color.background_green_selector) ColorStateList greenSelector;
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindColor {
    @ColorRes int value();
}
