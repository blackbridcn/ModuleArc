package org.bindview.annotation.field.values;

import androidx.annotation.DimenRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: yuzzha
 * Date: 2019/5/21 10:04
 * Description:
 * Remark:
 * <p>
 * BindDimen(R.dimen.horizontal_gap) int gapPx;
 * BindDimen(R.dimen.horizontal_gap) float gap;
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindDimen {
    @DimenRes int value();
}
