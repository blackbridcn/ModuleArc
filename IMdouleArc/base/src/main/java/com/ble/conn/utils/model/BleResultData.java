package com.ble.conn.utils.model;

/**
 * File: BleResultData.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 21:17
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-01-01 : Create BleResultData.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class BleResultData {
    /**
     * 结果标签 ; true 表示Ble应答操作成功; false 表示Ble应答操作失败或者程序报错;
     */
    private boolean result;

    /**
     * 开发者模式下Toast
     * 当result 为false时,才会有内容，用来显示详细的错误信息
     */
    private int msg;
    /**
     * Toast 给用户的回显提示信息
     */
    private int errorRes = -1;

    /**
     * 命令类型详见：
     * <p>
     */
    private byte commType;

    public BleResultData() {
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msgRes) {
        this.msg = msgRes;
    }

    public int getErrorRes() {
        return errorRes;
    }

    public void setErrorRes(int errorRes) {
        this.errorRes = errorRes;
    }

    public byte getCommType() {
        return commType;
    }

    public void setCommType(byte commType) {
        this.commType = commType;
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", errorRes=" + errorRes +
                ", commType=" + commType +
                '}';
    }
}

