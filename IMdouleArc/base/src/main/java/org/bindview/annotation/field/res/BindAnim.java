package org.bindview.annotation.field.res;


import androidx.annotation.AnimRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: yuzzha
 * Date: 2019-06-28 15:49
 * Description: ${DESCRIPTION}
 * Remark:
 *
 * BindAnim(R.anim.fade_in)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindAnim {

    @AnimRes int value();
}
