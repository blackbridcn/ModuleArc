package com.ble.conn.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    public static final String ALGORITHM = "AES";
    public static final String TRANSFORMATION = "AES/ECB/NoPadding";

    public static byte[] encrypt(byte[] content, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        if (content.length < 16) {
            byte[] bytes = new byte[16];
            for (int i = 0; i < bytes.length - content.length; i++) {
                bytes[i + content.length] = 0x00;
            }
            for (int i = 0; i < content.length; i++) {
                bytes[i] = content[i];
            }
            content = bytes;
        }
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, ALGORITHM));
        byte[] output = cipher.doFinal(content);
        return output;
    }

    /**
     * 执行解密
     *
     * @param content
     * @param key     长度必须为16、24、32位，即128bit、192bit、256bit
     * @return
     * @throws
     */
    public static byte[] decrypt(byte[] content, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, ALGORITHM));
        byte[] output = cipher.doFinal(content);
        return output;
    }

    /**
     * 反向字节数组
     *
     * @param hexString
     * @return
     */
    public static byte[] toDestByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];

        for (int i = 0; i < len; ++i) {
            try {
                result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        }
        byte[] bytes = new byte[result.length];
        for (int i = 0; i < result.length; i++) {
            bytes[i] = result[result.length - 1 - i];
        }
        return bytes;
    }
}
