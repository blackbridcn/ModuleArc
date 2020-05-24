package org.utils;

import java.nio.charset.Charset;

public class RC4Util {

    public static String decry_RC4(byte[] data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return asString(RC4Base(data, key));
    }

    public static String decry_RC4(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return new String(RC4Base(HexString2Bytes(data), key), Charset.forName("UTF-8"));
    }

    public static byte[] encry_RC4_byte(String data, String key) throws Exception {
        if (data == null || key == null) {
            return null;
        }
        byte b_data[] = data.getBytes("UTF-8");
        return RC4Base(b_data, key);
    }

    public static String encry_RC4_string(String data, String key) throws Exception {
        if (data == null || key == null)
            return null;
        byte[] cr4byte = encry_RC4_byte(data, key);
        return toHexString(asString(cr4byte));
    }

    private static String asString(byte[] buf) {
        StringBuffer strbuf = new StringBuffer(buf.length);
        for (int i = 0; i < buf.length; i++) {
            strbuf.append((char) buf[i]);
        }
        return strbuf.toString();
    }

    private static byte[] initKey(String aKey) {
        byte[] b_key = aKey.getBytes();
        byte state[] = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (b_key == null || b_key.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % b_key.length;
        }
        return state;
    }

    private static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch & 0xFF);
            if (s4.length() == 1) {
                s4 = '0' + s4;
            }
            str = str + s4;
        }
        return str;// 0x��ʾʮ������
    }

    private static byte[] HexString2Bytes(String src) {
        int size = src.length();
        byte[] ret = new byte[size / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < size / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        char _b0 = (char) Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (char) (_b0 << 4);
        char _b1 = (char) Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    private static byte[] RC4Base(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte key[] = initKey(mKkey);
        int xorIndex;
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        //String key = "12341ab0d3eee56b725e3e29999762a8";
        String key = "CB1712345678";
        String data = "[999985,V123,20180225102513,20180226102513]";
        String rc4 = "CB01" + RC4Util.encry_RC4_string(data, key).toUpperCase();
        System.out.println(rc4);

        String result = "CB0105FC4CBA783CA758D1E42F879B0183A52C3EE1863D3B329494C2EE17BD5027CE16BA308500C3FC8CB78C66";

        //System.out.println(TextUtils.equals(result, rc4));

        System.out.println(RC4Util.decry_RC4(
                "CB0105FC4CBA783CA758D1E42F879B0183A52C3EE1863D3B329494C2EE17BD5027CE16BA308500C3FC8CB78C66".replaceFirst("CB01",""),
                key));
    }
}