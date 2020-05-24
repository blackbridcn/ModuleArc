package com.ble.conn.task.command;

import com.ble.conn.task.receiver.BleMsgReceiver;
import com.ble.conn.utils.BleObservable;

import org.bluetooth.ble.callback.BleMsgCallback;

/**
 * File: BleCommand.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 20:31
 * Description:  PEAKE Ble 通信命令抽象类  父类
 * <p>
 * 命令模式中 Command  ：声明执行命令的接口
 * -----------------------------------------------------------------
 * 2020-01-01 : Create BleCommand.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public abstract class BleCommand {

    protected BleMsgCallback msgCallback;
    protected BleMsgReceiver receiver;
    protected BleObservable listener;
    protected byte[] response;

    public BleCommand(BleMsgReceiver receiver, byte[] response, BleObservable listener) {
        this.receiver = receiver;
        this.listener = listener;
        this.response = response;
    }

    public BleCommand(BleMsgReceiver receiver, BleMsgCallback msgCallback) {
        this.receiver = receiver;
        this.msgCallback = msgCallback;
    }

    // 抽象命令执行方法
    public abstract void Action();
}
