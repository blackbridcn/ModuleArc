package com.ble.conn.task.invoker;

import com.ble.conn.task.command.BleCommand;

/**
 * File: Invoker.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 20:41
 * Description: TODO 负责调用命令对象执行请求
 * -----------------------------------------------------------------
 * 2020-01-01 : Create Invoker.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class Invoker {

    private BleCommand command;

    public Invoker(BleCommand command) {
        this.command = command;
    }

    public void setCommand(BleCommand command) {
        this.command = command;
    }

    /**
     * 具体执行类
     */
    public void execute() {
        command.Action();
    }
}
