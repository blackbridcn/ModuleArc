package org.bindview.annotation.field.values;

import androidx.annotation.BoolRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: yuzzha
 * Date: 2019-06-28 16:08
 * Description: ${DESCRIPTION}
 * Remark:
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindBool {
    @BoolRes int value();
}
