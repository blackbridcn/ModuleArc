package com.ble.conn.client;

import androidx.annotation.NonNull;

import com.base.R;
import com.ble.BleConstants;
import com.ble.conn.task.command.BleCommand;
import com.ble.conn.task.command.concrete.ReadBleSettingResponse;
import com.ble.conn.task.command.concrete.ReadCardIdResponse;
import com.ble.conn.task.command.concrete.ReadCheckLinkResponse;
import com.ble.conn.task.command.concrete.ReadEncryptKeyResponse;
import com.ble.conn.task.command.concrete.WriteBleSettingMsg;
import com.ble.conn.task.command.concrete.WriteCardIdMsg;
import com.ble.conn.task.command.concrete.WriteCheckLinkMsg;
import com.ble.conn.task.command.concrete.WriteEncryptKeyMsg;
import com.ble.conn.task.invoker.Invoker;
import com.ble.conn.task.receiver.BleMsgReceiver;
import com.ble.conn.utils.BleMsgUtlis;
import com.ble.conn.utils.BleObservable;
import com.ble.conn.utils.model.BleResultData;

import org.bluetooth.ble.callback.BleMsgCallback;

import java.util.concurrent.atomic.AtomicReference;

public class BleManager {

    private static class BleManagerHplder {
        private static final BleManager INSATNCE = new BleManager();
    }

    private final BleMsgReceiver receiver;

    public static BleManager getInstance() {
        return BleManagerHplder.INSATNCE;
    }

    private BleManager() {
        receiver = new BleMsgReceiver();
    }

    /**
     * 发送握手秘钥信息
     * [2.7  发送加密密钥 ]
     *
     * @param bleMsgCallback
     */
    public void sendShakeKeyMsg(@NonNull BleMsgCallback bleMsgCallback) {
        excuteCommand(new WriteEncryptKeyMsg(receiver, bleMsgCallback));
    }

    /**
     * 握手成功后，发送卡号进行 开锁
     * [2.9  发送加密ID号]
     *
     * @param cardNo      卡号
     * @param msgCallback
     */
    public void sendCardMsg(@NonNull String cardNo, BleMsgCallback msgCallback) {
        excuteCommand(new WriteCardIdMsg(receiver, msgCallback, cardNo));
    }

    /**
     * 设置Ble蓝牙名称、类型、信号强度 命令
     * [ 2.6  设置蓝牙模块名称和发射功率 ]
     * <p>
     * 备注：本条命令为管理员命令， 生产部门和客户管理员才有使用本条命令的权限
     *
     * @param newBleName  Ble蓝牙名称
     * @param bleType     Ble蓝牙类型 详细见 BleConstants.BLE_TYPE
     * @param rssiType    信号强度等级参数类型 详细见文档 《PK-R3X7RNB NFC、蓝牙读卡器接口文件》
     * @param msgCallback
     */
    public void sendSetBleInfo(@NonNull String newBleName, int bleType, int rssiType, BleMsgCallback msgCallback) {
        excuteCommand(new WriteBleSettingMsg(receiver, msgCallback, newBleName, bleType, rssiType));
    }

    /**
     * 2.1  链路检查 命令
     *
     * @param msgCallback
     */
    public void sendCheckLinkMsg(BleMsgCallback msgCallback) {
        excuteCommand(new WriteCheckLinkMsg(receiver, msgCallback));
    }

    public BleResultData handleSecretResponse(byte[] response) {
        AtomicReference<BleResultData> resultBean = new AtomicReference<BleResultData>();
        excuteCommand(new ReadEncryptKeyResponse(receiver, response, (results) -> resultBean.set(results)));
        return resultBean.get();
    }

    public BleResultData handleCardResponse(byte[] response) {
        AtomicReference<BleResultData> resultBean = new AtomicReference<BleResultData>();
        excuteCommand(new ReadCardIdResponse(receiver, response, (BleObservable<BleResultData>) (results)-> resultBean.set(results)));
        return resultBean.get();
    }

    public BleResultData handleSettingBelResponse(byte[] response) {
        AtomicReference<BleResultData> resultBean = new AtomicReference<BleResultData>();
        excuteCommand(new ReadBleSettingResponse(receiver, response, (BleObservable<BleResultData>) (results) ->
                resultBean.set(results)));
        return resultBean.get();
    }

    public BleResultData handleCheckLinkResponse(byte[] response) {
        AtomicReference<BleResultData> resultBean = new AtomicReference<BleResultData>();
        excuteCommand(new ReadCheckLinkResponse(receiver, response, (results) ->
                resultBean.set(results)));
        return resultBean.get();
    }

    private void excuteCommand(BleCommand command) {
        Invoker invoker = new Invoker(command);
        invoker.execute();
    }

    /**
     * 解析读卡器应答/回执消息
     *
     * @param responseData Ble设置应答消息
     * @return ResultData解析结果
     */
    public BleResultData handleBleResponeMsg(byte[] responseData) {
        byte[] response = BleMsgUtlis.restoreSpecialChar(responseData);
        BleResultData resultData = null;
        if (response != null && response.length > 1) {
            switch (response[0]) {
                case BleConstants.BLE_RESPONSE_FLAGE://回执消息
                    if (response[1] == BleConstants.BLE_SET_BLEINFO_MSG_KEY) {//修改蓝牙设置信息
                        resultData = handleSettingBelResponse(response);
                    } else if (response[1] == BleConstants.BLE_CHECK_LINK_MSG_KEY) //链路检查
                        resultData = handleCheckLinkResponse(response);
                    break;
                case BleConstants.BLE_RESPONSE_CARD_FLAGE://开门状态
                    resultData = handleCardResponse(response);
                    break;
                case BleConstants.BLE_RESPONSE_SHAKE_FLAGE://握手秘钥
                    resultData = handleSecretResponse(response);
                    break;
            }
        } else {
            resultData = new BleResultData();
            resultData.setResult(false);
            resultData.setCommType(BleConstants.BLE_READE_ERROR_MSG_KEY);
            resultData.setErrorRes(R.string.base_operate_failed);
            resultData.setMsg(R.string.base_ble_read_ble_msg_lose);
        }
        return resultData;
    }

}
