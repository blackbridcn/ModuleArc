package com.ble.conn.task.receiver;

import com.base.R;
import com.ble.BleConstants;
import com.ble.conn.utils.AESUtils;
import com.ble.conn.utils.BleMsgUtlis;
import com.ble.conn.utils.BleObservable;
import com.ble.conn.utils.model.BleResultData;

import org.bluetooth.ble.BleClent;
import org.bluetooth.ble.callback.BleMsgCallback;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * File: BleMsgReceiver.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 20:42
 * Description: TODO  Receiver：BleMsg 命令的具体执行者;
 * -----------------------------------------------------------------
 * 2020-01-01 : Create BleMsgReceiver.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class BleMsgReceiver {
    /**
     * 发送握手秘钥->Ble设备
     * <p>
     * 生成秘钥信息,并调用发送接口发送数据；
     *
     * @param msgCallback
     */
    public void writeSecretMsgAction(BleMsgCallback msgCallback) {
        byte[] shakeMsg = null;
        try {
            shakeMsg = BleMsgUtlis.getShakeMsg();
        } catch (Exception e) {
            e.printStackTrace();
            if (msgCallback != null)
                msgCallback.sendMsgError(e.getMessage(), R.string.base_ble_generate_shake_error);
            return;
        }
        writeMsgToBle(shakeMsg, msgCallback);
    }

    /**
     * 发送CardId ->Ble设备
     * <p>
     * 将CardId 按照协议转换为byte[]信息,并调用发送接口发送数据；
     *
     * @param msgCallback BleMsgCallback发送回调
     * @param cardNo      卡号
     */
    public void writeCardIdMsgAction(BleMsgCallback msgCallback, String cardNo) {
        byte[] cardMsg = null;
        try {
            cardMsg = BleMsgUtlis.getCardMsg(cardNo);
        } catch (Exception e) {
            e.printStackTrace();
            if (msgCallback != null)
                msgCallback.sendMsgError(e.getMessage(), R.string.base_ble_generate_cardmsg_error);
            return;
        }
        writeMsgToBle(cardMsg, msgCallback);
    }

    /**
     * 发送设置信息->Ble设备
     * <p>
     * 将设置参数转换为byte[],并调用发送接口发送数据；
     *
     * @param msgCallback
     * @param bleName     设置的ble名称
     * @param bleType     设置的ble 类型
     * @param rssiType    设置信号的等级代号
     */
    public void writeBleSettingMsgAction(BleMsgCallback msgCallback, String bleName, int bleType, int rssiType) {
        byte[] bleInof = null;
        try {
            bleInof = BleMsgUtlis.getBleSettingMsg(bleName, bleType, rssiType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            if (msgCallback != null)
                msgCallback.sendMsgError(e.getMessage(), R.string.base_ble_generate_bleinfo_error);
            return;
        }
        writeMsgToBle(bleInof, msgCallback);
    }

    /**
     * 发送链路检查命令 ->Ble设备
     *
     * @param msgCallback
     */
    public void writeCheckLinkMsgAction(BleMsgCallback msgCallback) {
        byte[] check = null;
        try {
            check = BleMsgUtlis.getCheckLinkMsg();
        } catch (Exception e) {
            e.printStackTrace();
            msgCallback.sendMsgError(e.getMessage(), R.string.base_ble_check_link_comm_error);
            return;
        }
        writeMsgToBle(check, msgCallback);
    }

    /**
     * 发送byte[] 到Ble设备
     *
     * @param bleMsg      通信数据byte[]
     * @param msgCallback
     */
    private void writeMsgToBle(byte[] bleMsg, BleMsgCallback msgCallback) {
        if (bleMsg != null)
            BleClent.getInstance().sendMsgToBleDevice(bleMsg, msgCallback);
    }

    /**
     * 解析发送卡号命令的回执
     *
     * @param response
     * @param listener
     */
    public void handleCardResponse(byte[] response, BleObservable<BleResultData> listener) {
        BleResultData bean = new BleResultData();
        bean.setCommType(BleConstants.BLE_CARD_ID_MSG_KEY);
        if (response != null && response.length >= 2 && listener != null) {
            if (response[1] == BleConstants.BLE_RESPONSE_SUCCESS) {
                bean.setResult(true);
                bean.setErrorRes(R.string.base_operate_success);
                listener.then(bean);
            } else if (response[1] == BleConstants.BLE_RESPONSE_FAIL) {
                bean.setResult(false);
                bean.setErrorRes(R.string.base_operate_failed);
                bean.setMsg(R.string.base_this_card_no_permission);
                listener.then(bean);
            } else {
                bean.setResult(false);
                bean.setErrorRes(R.string.base_operate_failed);
                bean.setMsg(R.string.base_ble_card_unknow_error);
                listener.then(bean);
            }
        } else if (listener != null) {
            bean.setResult(false);
            bean.setMsg(R.string.base_ble_card_msg_read_fail);
            bean.setErrorRes(R.string.base_operate_failed);
            listener.then(bean);
        }
    }

    /**
     * 解析发送握手秘钥后ble应答信息
     *
     * @param response
     * @param listener
     */
    public void handleSecretResponse(byte[] response, BleObservable<BleResultData> listener) {
        BleResultData bean = new BleResultData();
        bean.setCommType(BleConstants.BLE_ENCRYPT_KEY_MSG_KEY);
        byte[][] nextReceive = BleMsgUtlis.splitMsg(response);
        byte[] randomKey = BleMsgUtlis.generateEncryptKey(nextReceive[0]);
        byte[] decrypt = null;
        try {
            decrypt = AESUtils.decrypt(nextReceive[1], randomKey);
        } catch (Exception e) {
            e.printStackTrace();
            bean.setResult(false);
            bean.setMsg(R.string.base_ble_decrypt_shake_error);
            bean.setErrorRes(R.string.base_operate_failed);
            listener.then(bean);
            return;
        }
        if (Arrays.equals(BleConstants.PEAKE_BLE_SHAKE_KEY, decrypt)) {
            bean.setResult(true);
        } else {
            bean.setResult(false);
            bean.setMsg(R.string.base_ble_shake_noequals_error);
            bean.setErrorRes(R.string.base_operate_failed);
        }
        listener.then(bean);
    }

    /**
     * 解析读卡器收到 设置蓝牙信息命令 应答回执信息
     *
     * @param response
     * @param listener
     */
    public void handleBleSettingResponse(byte[] response, BleObservable<BleResultData> listener) {
        BleResultData bean = new BleResultData();
        bean.setCommType(response[1]);
        switch (response[2]) {
            case BleConstants.BLE_RESPONSE_SUCCESS:
                bean.setResult(true);
                bean.setErrorRes(R.string.base_ble_resetinfo_ok);
                break;
            case BleConstants.BLE_RESPONS_FAIL_CRC:
                bean.setResult(false);
                bean.setMsg(R.string.base_ble_resetinfo_crc_error);
                bean.setErrorRes(R.string.base_ble_resetinfo_error);
                break;
            default:
                bean.setResult(false);
                bean.setMsg(R.string.base_ble_return_unknow_error);
                bean.setErrorRes(R.string.base_ble_resetinfo_error);
                break;
        }
        if (listener != null) {
            listener.then(bean);
        }
    }

    /**
     * 解析读卡器收到 链路检查命令 后的 应答 回执信息
     *
     * @param response 读卡器返回消息
     * @param listener 消息解析结果回调
     */
    public void handleCheckLinkResponse(byte[] response, BleObservable<BleResultData> listener) {
        BleResultData bean = new BleResultData();
        bean.setCommType(response[1]);
        switch (response[2]) {
            case BleConstants.BLE_RESPONSE_SUCCESS:
                bean.setResult(true);
                bean.setErrorRes(R.string.base_ble_check_link_ok);
                break;
            case BleConstants.BLE_RESPONS_FAIL_CRC:
                bean.setResult(false);
                bean.setMsg(R.string.base_ble_resetinfo_crc_error);
                bean.setErrorRes(R.string.base_ble_check_link_erroe);
                break;
            default:
                bean.setResult(false);
                bean.setMsg(R.string.base_ble_return_unknow_error);
                bean.setErrorRes(R.string.base_ble_check_link_erroe);
                break;
        }
        if (listener != null) {
            listener.then(bean);
        }
    }
}
