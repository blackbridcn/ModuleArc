package com.ble.conn.task.command.concrete;

import com.ble.conn.task.command.BleCommand;
import com.ble.conn.task.receiver.BleMsgReceiver;
import com.ble.conn.utils.BleObservable;
import com.ble.conn.utils.model.BleResultData;

/**
 * File: ReadBleSettingResponse.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 20:40
 * Description: TODO  解析PEAKE的BLE设备返回的 发送设置命令后的BLE应答信息
 * -----------------------------------------------------------------
 * 2020-01-01 : Create ReadBleSettingResponse.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class ReadBleSettingResponse extends BleCommand {

    public ReadBleSettingResponse(BleMsgReceiver receiver, byte[] response, BleObservable<BleResultData> listener) {
        super(receiver, response, listener);
    }

    @Override
    public void Action() {
        this.receiver.handleBleSettingResponse(this.response, this.listener);
    }

}
