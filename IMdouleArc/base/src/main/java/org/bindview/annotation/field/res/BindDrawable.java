package org.bindview.annotation.field.res;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: yuzzha
 * Date: 2019/5/21 10:47
 * Description: ${DESCRIPTION}
 * Remark:
 * <p>
 * BindDrawable(R.drawable.placeholder)
 * Drawable placeholder;
 * BindDrawable(value = R.drawable.placeholder, tint = R.attr.colorAccent)
 * Drawable tintedPlaceholder;
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindDrawable {
    /**
     *
     */
    @DrawableRes int value();

    /**
     * Color attribute resource ID that is used to tint the drawable.
     */
    @AttrRes int tint() default -1;
}
