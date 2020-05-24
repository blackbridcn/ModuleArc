package com.ble.conn.utils;

import android.text.TextUtils;

import com.ble.BleConstants;
import com.ble.conn.utils.callback.ParseBleMsgCallback;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BleMsgUtlis {

    public static Random sRandom = new Random();
    private static List<Byte> temptList;
    private static boolean mIsStart;
    private static boolean mIsEnd;
    private static int next;       //0标示无，1标示差一个，2标示差两个

    /**
     * 获取需要加密密钥
     *
     * @return byte[]
     * @throws
     */
    public static byte[] getShakeMsg() throws Exception {
        return buildMsg(BleConstants.BLE_ENCRYPT_KEY_MSG_KEY, BleConstants.PEAKE_BLE_SHAKE_KEY);
    }

    /**
     * 对cardNo进行加密处理
     *
     * @param cardNo byte[]
     * @return
     * @throws
     */
    public static byte[] getCardMsg(byte[] cardNo) throws Exception {
        return buildMsg(BleConstants.BLE_CARD_ID_MSG_KEY, cardNo);
    }

    /**
     * 对cardNo进行加密处理
     *
     * @param cardNo String
     * @return
     * @throws
     */
    public static byte[] getCardMsg(String cardNo) throws Exception {
        return buildMsg(BleConstants.BLE_CARD_ID_MSG_KEY, AESUtils.toDestByte(cardNo));
    }

    public static int getBleType(String typeChar) {
        int bleType = -1;
        String[] type = BleConstants.BLE_TYPE;
        for (int i = 0; i < type.length; i++) {
            if (TextUtils.equals(typeChar, type[i])) {
                bleType = i;
                break;
            }
        }
        return bleType;
    }


    /**
     * 2.6  设置蓝牙模块名称和发射功率
     * <p>
     * 备注：考虑到蓝牙数据传输信息量 和 这条命令的属性[管理员命令] ，
     * 本条命令没有采取刷卡时的随机数字和握手秘钥加密，只是保留了crc16校验；
     * <p>
     * 基本命令结构为： 命令类型+ 蓝牙发射功率+ 蓝牙模块名称长度+蓝牙模块名称
     * </p>
     *
     * @param bleName  新的蓝牙名称
     * @param bleType  蓝牙类型 详细参数见BelConstants.BLE_TYPE
     * @param rssiType 蓝牙发射功率参数 详细参数见文档
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] getBleSettingMsg(String bleName, int bleType, int rssiType) throws UnsupportedEncodingException {
        String name = BleConstants.PEAKE_BLEDEVICE_PRE_NAME + BleConstants.BLE_TYPE[bleType] + bleName;
        byte[] nameBytes = name.getBytes("UTF-8");
        int length = nameBytes.length;
        byte[] result = new byte[length + 3];
        result[0] = BleConstants.BLE_SET_BLEINFO_MSG_KEY;
        result[1] = (byte) rssiType;
        result[2] = (byte) length;
        System.arraycopy(nameBytes, 0, result, 3, length);
        return replaceSpecialChar(result);
    }

    /**
     * 获取链路检测命令
     *
     * @return
     * @throws
     */
    public static byte[] getCheckLinkMsg() throws Exception {
        return buildMsg(BleConstants.BLE_CHECK_LINK_MSG_KEY, new byte[0]);
    }

    /**
     * 解析BLE从机返回的消息
     * 备注：读取完整的数据【处理分包数据】，对数据进行解析，然后通过接口回调将解析结果发出去
     *
     * @param value
     */
    public static void readWholeMsg(byte[] value, ParseBleMsgCallback callback) {
        if (next != 0) {
            for (int i = 0; i < next; i++) {
                temptList.add(value[i]);
            }
            next = 0;
            mIsStart = false;
            mIsEnd = false;
            byte[] result = new byte[temptList.size()];
            for (int i = 0; i < temptList.size(); i++) {
                result[i] = temptList.get(i);
            }
            callback.onWholeMsg(result);
            temptList = null;
        }
        int startIndex = 0;
        int endIndex = value.length;
        for (int i = 0; i < value.length; i++) {
            if (value[i] == BleConstants.STX) {
                if (temptList != null) temptList.clear();
                else
                    temptList = new ArrayList<>();
                startIndex = i;
                mIsStart = true;
                mIsEnd = false;
            }
            if (value[i] == BleConstants.ETX) {
                endIndex = i + 1;
                mIsEnd = true;
                int y = value.length - 1 - i;
                next = y == 0 ? 2 : (y == 1 ? 1 : 0);
            }
        }
        if (mIsStart) {
            for (int i = startIndex; i < endIndex; i++) {
                assert temptList != null;
                temptList.add(value[i]);
            }
        }
        if (mIsEnd && next == 0) {
            for (int i = endIndex; i < endIndex + 2; i++) {
                assert temptList != null;
                temptList.add(value[i]);
            }
            mIsStart = false;
            mIsEnd = false;
            byte[] result = new byte[temptList.size()];
            for (int i = 0; i < temptList.size(); i++) {
                result[i] = temptList.get(i);
            }
            callback.onWholeMsg(result);
            temptList = null;
        }
        if (mIsEnd && next == 1) {
            for (int i = endIndex; i < endIndex + 1; i++) {
                assert temptList != null;
                temptList.add(value[i]);
            }
        }
    }


    /**
     * 构建PEAKE BLE蓝牙消息
     * <p>
     * PEAKE 蓝牙消息基本结构：
     * 报头/起始符[1byte]  +   命令类型  +  8位加密秘钥[8位随机秘钥] + 经过完整秘钥加密后的Msg  +  报尾/结束符[1byte]  +  CRC消息校验[包括报头和报尾]
     * 1byte 固定字符0x7E  +   1 byte   +      8byte               +  nByte                  +  1byte 固定字符0x7F  +   2byte[CRC校验和 低位在前高位在后]
     * <p>
     *
     * @param preFlag
     * @param content
     * @return
     * @throws
     */
    public static byte[] buildMsg(byte preFlag, byte[] content) throws Exception {
        //8个随机字节数
        byte[] randomKey = generateRandom8Bytes();
        byte[] encryptKey = generateEncryptKey(randomKey);
        byte[] encryptContent = AESUtils.encrypt(content, encryptKey);
        byte[][] msg = new byte[2][];
        msg[0] = randomKey;
        msg[1] = encryptContent;
        byte[] bytes = composeCommand(preFlag, msg);
        return replaceSpecialChar(bytes);
    }

    /**
     * 生成8位随机秘钥
     *
     * @return byte[]
     */
    public static byte[] generateRandom8Bytes() {
        byte[] bytes = new byte[8];
        sRandom.nextBytes(bytes);
        return bytes;
    }

    /**
     * 拼接一个完整公钥：固定公钥 + 随机公钥
     * 生成后八位随机的key
     *
     * @return
     */
    public static byte[] generateEncryptKey(byte[] randomKey) {
        byte[] mKeys = randomKey;
        int len = BleConstants.PEAKE_BLE_ENCRYPT_KEY.length;
        byte[] newBytes = new byte[len + mKeys.length];
        //newStr = str1;数组是引用类型
        for (int x = 0; x < len; x++) {
            newBytes[x] = BleConstants.PEAKE_BLE_ENCRYPT_KEY[x];
        }
        for (int y = 0; y < mKeys.length; y++) {
            newBytes[len + y] = mKeys[y];
        }
        return newBytes;
    }

    /**
     * 发送前合并多条信息
     * 备注：合并策略 1 byte消息类型+ 8 byte 随机公钥+ 消息body
     *
     * @param bytes
     * @return 合并后的整条信息
     */
    public static byte[] composeCommand(byte flag, byte[][] bytes) {
        int length = 1;
        for (int i = 0; i < bytes.length; i++) {
            length += bytes[i].length;
        }
        byte[] results = new byte[length];
        results[0] = flag;
        int sum = 1;
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < bytes[i].length; j++) {
                results[sum + j] = bytes[i][j];
            }
            sum += bytes[i].length;
        }
        return results;
    }

    /**
     * 主动发送的消息进行 转义字符处理 + CRC 串口通信校验
     *
     * @param command
     * @return
     */
    public static byte[] replaceSpecialChar(byte[] command) {
        int nI, nOffset;
        int nCrc16, nCrc16H, crcout;
        byte[] outBuf = new byte[256];
        outBuf[0] = BleConstants.STX; // 报文头
        nOffset = 1;
        for (nI = 0; nI < command.length; nI++) {
            switch (command[nI]) {
                case BleConstants.STX: // 如果报文中有STX字符，将STX替换成X字符，并在X之前加入转义字符DLE
                    outBuf[nOffset] = (byte) (BleConstants.DLE - 256);
                    nOffset++;
                    outBuf[nOffset] = BleConstants.X;
                    nOffset++;
                    break;
                case BleConstants.ETX: // 如果报文中有ETX字符，将ETX替换成Y字符，并在Y之前加入转义字符DLE
                    outBuf[nOffset] = (byte) (BleConstants.DLE - 256);
                    nOffset++;
                    outBuf[nOffset] = BleConstants.Y;
                    nOffset++;
                    break;
                case (byte) (BleConstants.DLE - 256): // 如果报文中有DLE字符，将DLE替换成Z字符，并在Z之前加入转义字符DLE
                    outBuf[nOffset] = (byte) (BleConstants.DLE - 256);
                    nOffset++;
                    outBuf[nOffset] = BleConstants.Z;
                    nOffset++;
                    break;
                default: {
                    outBuf[nOffset] = command[nI];
                    nOffset++;
                    break;
                }
            }
        }
        // 写报文尾
        outBuf[nOffset] = BleConstants.ETX;
        nOffset++;
        // 计算校验和，包括报头报尾
        crcout = CalcCRC161(outBuf, nOffset, 0xffff); // 在接收和发送端的初值必须一样
        // 将校验和分成高低位送入发送buffer
        int aa = crcout & 0xff;
        if (aa > 127) {
            outBuf[nOffset] = (byte) (aa - 256);
        } else {
            outBuf[nOffset] = (byte) (aa);
        }
        nOffset++;
        int bb = ((crcout & 0xff00) >> 8);
        if (bb > 127) {
            outBuf[nOffset] = (byte) (bb - 256);
        } else {
            outBuf[nOffset] = (byte) (bb);
        }
        nOffset++;
        // 转换完毕
        outBuf = copyOfRange(outBuf, 0, nOffset);
        return outBuf; // 返回转换后的数据长度（原始数据长度）
    }

    /**
     * 恢复 转义字符处理
     * <p>
     * 此函数在接收时调用
     *
     * @param inBuf
     * @return
     */
    public static byte[] restoreSpecialChar(byte[] inBuf) {
        int nOutLen;
        int nOffset = 0;
        int nI, blSkip = 0;
        byte[] outBuf = new byte[256];
        if (inBuf.length < 3) {
//			Log.e("PackInProcess()", "字节长度不足:" + new String(inBuf));
            return inBuf;
        }
        int a = inBuf.length;
        // 对报文进行转义处理,对接收buffer的后3个字节（ETX,两位校验和）不做处理,
        for (nI = 1; nI < inBuf.length - 3; nI++) {
            // 如果前一个字符为DLE时，忽略下一个字符
            if (blSkip != 0) {
                blSkip = 0;
                continue;
            }
            // 如果是转义字符，则下一个为数据，将DLE去掉
            if (inBuf[nI] == BleConstants.DLE) {
                switch (inBuf[nI + 1]) {
                    case BleConstants.X:
                        outBuf[nOffset] = BleConstants.STX;
                        nOffset++;
                        blSkip = 1;
                        break;

                    case BleConstants.Y:
                        outBuf[nOffset] = BleConstants.ETX;
                        nOffset++;
                        blSkip = 1;
                        break;

                    case BleConstants.Z:
                        outBuf[nOffset] = (byte) (BleConstants.DLE - 256);
                        nOffset++;
                        blSkip = 1;
                        break;
                }
            } else {
                outBuf[nOffset] = inBuf[nI];
                nOffset++;
            }
        }
        outBuf = copyOfRange(outBuf, 0, nOffset);
        return outBuf;
    }


    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

    /**
     * 接收到消息后分离 动态秘钥 和 密文口令
     *
     * @param bytes
     * @return 秘钥和密文口令的二维字节数组
     */
    public static byte[][] splitMsg(byte[] bytes) {
        if (bytes[0] != 0x9) {
            return null;
        }
        byte[][] results = new byte[2][];
        results[0] = new byte[8];
        results[1] = new byte[16];
        for (int i = 1; i < bytes.length; i++) {
            if (i < 9) {
                results[0][i - 1] = bytes[i];
            } else {
                results[1][i - 9] = bytes[i];
            }
        }
        return results;
    }

    /**
     * CalcCRC161
     *
     * @param data
     * @param len
     * @param preval
     * @return
     */
    public static int CalcCRC161(byte[] data, int len, int preval) {
        int ucCRCHi = (preval & 0xff00) >> 8;
        int ucCRCLo = preval & 0x00ff;
        int iIndex;
        for (int i = 0; i < len; ++i) {
            iIndex = (ucCRCLo ^ data[0 + i]) & 0x00ff;
            ucCRCLo = ucCRCHi ^ BleConstants.crc16_tab_h[iIndex];
            ucCRCHi = BleConstants.crc16_tab_l[iIndex];
        }
        return ((ucCRCHi & 0x00ff) << 8) | (ucCRCLo & 0x00ff) & 0xffff;
    }
}
