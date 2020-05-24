package com.ble.conn.task.command.concrete;

import com.ble.conn.task.command.BleCommand;
import com.ble.conn.task.receiver.BleMsgReceiver;

import org.bluetooth.ble.callback.BleMsgCallback;

/**
 * File: WriteBleSettingMsg.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 20:36
 * Description: 设置 PEAKE的BLE设备信息的命令【蓝牙门禁名/类型/信号强度】
 * -----------------------------------------------------------------
 * 2020-01-01 : Create WriteBleSettingMsg.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class WriteBleSettingMsg extends BleCommand {

    private String bleName;
    private int bleType, rssiType;

    public WriteBleSettingMsg(BleMsgReceiver receiver, BleMsgCallback msgCallback, String bleName, int bleType, int rssiType) {
        super(receiver, msgCallback);
        this.bleName = bleName;
        this.bleType = bleType;
        this.rssiType = rssiType;
    }

    @Override
    public void Action() {
        this.receiver.writeBleSettingMsgAction(msgCallback, this.bleName, this.bleType, this.rssiType);
    }
}
