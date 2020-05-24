package org.bluetooth.ble.utils;

/**
 * Author: yuzzha
 * Date: 2019/4/25 18:35
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class StringUtils {

    /**
     * 从十六进制字符串到字节数组转换
     *
     * @param hexstr 十六进制字符串
     * @return
     */

    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }
}
