package org.imodule.uitls;

/**
 * File: StringUtils.java
 * Author: yuzhuzhang
 * Create: 2019-12-29 19:06
 * Description: TODO
 * -----------------------------------------------------------------
 * 2019-12-29 : Create StringUtils.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class StringUtils {

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return cs != null && cs.length() > 0;
    }
}
