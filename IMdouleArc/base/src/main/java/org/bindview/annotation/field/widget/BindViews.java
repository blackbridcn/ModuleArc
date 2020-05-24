package org.bindview.annotation.field.widget;


import androidx.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: yuzzha
 * Date: 2019/5/21 12:05
 * Description: ${DESCRIPTION}
 * Remark:
 * <p>
 *
 * @BindViews({ R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5})
 * TextView[] textViews;
 * <p>
 * @BindViews({ R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5})
 * List<TextView> textViews;
 * </p>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindViews {

    @IdRes int[] value();
}
