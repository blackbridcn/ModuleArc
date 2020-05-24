package com.ble.conn.task.command.concrete;

import com.ble.conn.task.command.BleCommand;
import com.ble.conn.task.receiver.BleMsgReceiver;
import com.ble.conn.utils.BleObservable;
import com.ble.conn.utils.model.BleResultData;

/**
 * File: ReadCardIdResponse.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 20:39
 * Description: 解析PEAKE的BLE设备返回的 发送卡号命令的回文
 * -----------------------------------------------------------------
 * 2020-01-01 : Create ReadCardIdResponse.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class ReadCardIdResponse extends BleCommand {

    public ReadCardIdResponse(BleMsgReceiver receiver, byte[] response, BleObservable<BleResultData> listener) {
        super(receiver, response, listener);
    }

    @Override
    public void Action() {
        this.receiver.handleCardResponse(this.response, this.listener);
    }

}
