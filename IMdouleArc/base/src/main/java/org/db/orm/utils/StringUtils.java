package org.db.orm.utils;

/**
 * Author: yuzzha
 * Date: 2019-06-26 11:33
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class StringUtils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }
}
