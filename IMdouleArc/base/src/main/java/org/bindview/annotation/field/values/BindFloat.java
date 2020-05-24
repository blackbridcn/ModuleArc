package org.bindview.annotation.field.values;

import androidx.annotation.DimenRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: yuzzha
 * Date: 2019-06-28 16:20
 * Description: ${DESCRIPTION}
 * Remark:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindFloat {
    @DimenRes int value();
}
