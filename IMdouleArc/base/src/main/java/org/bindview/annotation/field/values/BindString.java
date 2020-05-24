package org.bindview.annotation.field.values;

import androidx.annotation.StringRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: yuzzha
 * Date: 2019/5/21 11:48
 * Description: ${DESCRIPTION}
 * Remark:
 * <p>
 * BindString(R.string.username_error) String usernameErrorText;
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindString {

    @StringRes int value();

}
