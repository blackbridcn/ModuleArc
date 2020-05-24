package com.ble.conn.utils.callback;

/**
 * Author: yuzzha
 * Date: 2019/5/5 10:56
 * Description: ${DESCRIPTION}
 * Remark:
 */
public interface ParseBleMsgCallback {

    public void onError(String errorMsg);


    public void onWholeMsg(byte[] result);

}
