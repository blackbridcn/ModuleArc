package org.patternlock.def.data

import androidx.annotation.ColorInt


/**
 * File: DefaultStyleDecorator.java
 * Author: yuzhuzhang
 * Create: 2020/2/27 5:18 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/2/27 : Create DefaultStyleDecorator.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
data class DefaultStyleDecorator(
        @ColorInt var normalColor: Int,
        @ColorInt var fillColor: Int,
        @ColorInt var hitColor: Int,
        @ColorInt var errorColor: Int,
        var lineWidth: Float
)