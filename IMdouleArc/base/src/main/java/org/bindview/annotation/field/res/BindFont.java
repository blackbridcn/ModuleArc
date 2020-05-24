package org.bindview.annotation.field.res;

import android.graphics.Typeface;

import androidx.annotation.FontRes;
import androidx.annotation.IntDef;
import androidx.annotation.RestrictTo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: yuzzha
 * Date: 2019-06-28 16:23
 * Description: ${DESCRIPTION}
 * Remark:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindFont {

    @FontRes int value();

    @TypefaceStyle int style() default Typeface.NORMAL;

    @IntDef({
            Typeface.NORMAL,
            Typeface.BOLD,
            Typeface.ITALIC,
            Typeface.BOLD_ITALIC
    })

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @interface TypefaceStyle {
    }
}
