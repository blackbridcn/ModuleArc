package org.bindview.annotation.field.res;

import androidx.annotation.DrawableRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: yuzzha
 * Date: 2019-06-28 15:57
 * Description: ${DESCRIPTION}
 * Remark:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindBitmap {
    @DrawableRes int value();
}
