package com.ble.help.mvp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import androidx.annotation.NonNull;

import com.base.R;
import com.ble.BleConstants;
import com.ble.conn.client.BleManager;
import com.ble.conn.utils.BleMsgUtlis;
import com.ble.conn.utils.ObjectHelp;
import com.ble.conn.utils.callback.DefualtScanCallback;
import com.ble.conn.utils.callback.ParseBleMsgCallback;
import com.ble.conn.utils.model.BleResultData;
import com.ble.help.param.BleEvent;

import org.bluetooth.ble.BleClent;
import org.bluetooth.ble.callback.BleConnCallback;
import org.bluetooth.ble.callback.BleMsgCallback;
import org.bluetooth.ble.model.BleDeviceData;
import org.utils.HandlerUtils;
import org.utils.StringUtils;
import org.utils.UIUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Author: yuzzha
 * Date: 2019/5/5 16:06
 * Description: 蓝牙 Model  公共 Modle
 * Remark:
 */
public class BelModel implements IBleContract.IOpenDoorModel, IBleContract.ISettingModel {

    //用户手动点击开锁按钮时间
    public long LAST_CLICK_TIME;

    private IBleContract.BasePresenter mBleEventPersenter;

    int BleEventType = -1;
    String newBleName;
    int bleType,rssiType;

    /**
     * 蓝牙连接 超时任务 [这个很重要]
     * 解决连接失败，并没有是回调的问题；
     */
    private Runnable timeOutTask = () -> {
        doErrorEventTask(R.string.base_ble_conn_timeout);
        mBleEventPersenter.doBleEventTimeOutTask(getCurrtEventType());
        BleEventType = BleEvent.EVENT_IDLE;//IBleEventModel.EVENT_IDLE_KEY_TYPE;
    };

    public BelModel(IBleContract.BasePresenter mBleEventPersenter) {
        this.mBleEventPersenter = mBleEventPersenter;
    }

    @Override
    public void doConnBlePreTask() {
        resetBleState();
        startConnTimeOutTask();
        mBleEventPersenter.doConnBlePreTask(getCurrtEventType());
    }


    @Override
    public void onConnBleNextTask(boolean isResult, int tipRes) {
        stopConnTimeOutTask();
        mBleEventPersenter.doConnBleFinshTask(isResult, getCurrtEventType());
        HandlerUtils.postTask(() -> makeToast(UIUtils.getString(tipRes)));
        //disConnBle();
        disConnAndClearBleGatt();
        BleEventType = BleEvent.EVENT_IDLE;//IBleEventModel.EVENT_IDLE_KEY_TYPE;
    }

    @Override
    public int getCurrtEventType() {
        return this.BleEventType;
    }

    /**
     * BLE 蓝牙扫描回调
     */
    private DefualtScanCallback callback = new DefualtScanCallback() {
        @Override
        public void onScanFinsh(List<BleDeviceData> lists) {
            if (lists.isEmpty()) {
                checkLocationService();
                mBleEventPersenter.doScanBleFinshTask();
            } else {
                if (isScanConnType(BleEventType)) {
                    onConnEvent(BleEventType, lists.get(0).getDevice());
                }
                mBleEventPersenter.doScanBleFinshTask(lists);
            }
        }
    };

    @Override
    public void doScanAndOpenDoorTask() {
        doScanTask(BleEvent.EVENT_ENCRYPT);
    }

    @Override
    public void doScanTask(@BleEvent int eventType) {
        resetBleState();
        if (isBleEnabled()) {
            BleEventType = eventType;
            mBleEventPersenter.doSacnBlePreTask();
            BleClent.getInstance().startScan(callback);
        } else {
            BleEventType = BleEvent.EVENT_IDLE;// IBleEventModel.EVENT_IDLE_KEY_TYPE;
            gotoBleSettingsPage();
        }
    }

    @Override
    public void doOnlyScanTask() {
        doScanTask(BleEvent.EVENT_ONLY_SCAN);//IBleEventModel.EVENT_ONLY_SCAN_TYPE);
    }

    @Override
    public void startConnTimeOutTask() {
        stopConnTimeOutTask();//清楚之前是超时任务
        HandlerUtils.postDelayTask(timeOutTask, IBleContract.BLE_CONN_TIMEOUT);
    }

    @Override
    public void stopConnTimeOutTask() {
        //清楚超时任务
        HandlerUtils.removeDelayTask(timeOutTask);
    }

    @Override
    public void onConnBle(String bleAddress, @BleEvent int eventType, BleConnCallback connCallback) {
        this.BleEventType = eventType;
        doConnBlePreTask();
        BleClent.getInstance().connect(bleAddress, connCallback);
    }

    @Override
    public void onConnBle(BluetoothDevice device, @BleEvent int eventType, BleConnCallback connCallback) {
        onConnBle(device, eventType, false, connCallback);
    }

    @Override
    public void onConnBle(BluetoothDevice device, @BleEvent int eventType, boolean autoConnect, BleConnCallback connCallback) {
        this.BleEventType = eventType;
        doConnBlePreTask();
        BleClent.getInstance().connect(device, autoConnect, connCallback);
    }

    @Override
    public void onConnBleOpenDoor(BluetoothDevice device) {
        if (isNotRepeatClick()) {
            onConnBle(device, BleEvent.EVENT_ENCRYPT /*IBleEventModel.EVENT_ENCRYPT_KEY_TYPE*/, false, connCallback);
        }
    }

    @Override
    public void doSetBelInfoTask(BluetoothDevice device, @NonNull String newBleName, int bleType, int rssiType) {
        if (isNotRepeatClick() && isValidName(newBleName)) {
            this.newBleName = newBleName;
            this.rssiType = rssiType;
            onConnBle(device, BleEvent.EVENT_SET_BLEINFO /*IBleEventModel.EVENT_SET_BLEINFO_TYPE*/, false, connCallback);
        }
    }

    @Override
    public void doCheckLinkTask(BluetoothDevice device) {
        if (isNotRepeatClick()) {
            onConnBle(device, BleEvent.EVENT_CHECK_LINK /*IBleEventModel.EVENT_CHECK_LINK_TYPE*/, true, connCallback);
        }
    }

    @Override
    public void sendMsgToBle(int bleEventType, BleMsgCallback msgCallback) {
        switch (bleEventType) {
            case BleEvent.EVENT_ENCRYPT/*.EVENT_ENCRYPT_KEY_TYPE*/:
                BleManager.getInstance().sendShakeKeyMsg(bleMsgCallback);
                break;
            case BleEvent.EVENT_CHECK_LINK /*IBleEventModel.EVENT_CHECK_LINK_TYPE*/:
                BleManager.getInstance().sendCheckLinkMsg(bleMsgCallback);
                break;
            case BleEvent.EVENT_SET_BLEINFO /*IBleEventModel.EVENT_SET_BLEINFO_TYPE*/:
                BleManager.getInstance().sendSetBleInfo(this.newBleName, this.bleType, this.rssiType, bleMsgCallback);
                break;
        }
    }

    @Override
    public String getCardId() {
        if (mBleEventPersenter instanceof IBleContract.IOpenDoorPresenter) {
            IBleContract.IOpenDoorPresenter persenter = (IBleContract.IOpenDoorPresenter) mBleEventPersenter;
            return persenter.getCardId();
        }
        return "";//mBleEventPersenter.getCardId();
    }

    /**
     * 解析Handle 远程BLE返回回文消息
     *
     * @param responeMsg byte[] 远程BLE 回文
     */
    @Override
    public void handleBleResponeMsg(byte[] responeMsg) {
        BleResultData resultData = BleManager.getInstance().handleBleResponeMsg(responeMsg);
        if (resultData.isResult()) {
            switch (resultData.getCommType()) {
                case BleConstants.BLE_ENCRYPT_KEY_MSG_KEY:
                    //TODO 握手秘钥成功  发送卡号进行校验开锁
                    BleManager.getInstance().sendCardMsg(ObjectHelp.checkNotEmpty(getCardId(), "CARD NO. "), bleMsgCallback);
                    break;

                case BleConstants.BLE_CARD_ID_MSG_KEY:
                    //TODO  开始成功后 恢复摇一摇传感器注册和View变化
                    onConnBleNextTask(resultData.isResult(), resultData.getErrorRes());
                    break;

                default:
                    //TODO  链路检查成功 BleConstants.BLE_CHECK_LINK_MSG_KEY /  设置蓝牙信息 BleConstants.BLE_SET_BLEINFO_MSG_KEY
                    onConnBleNextTask(resultData.isResult(), resultData.getErrorRes());
                    break;
            }
        } else {
            //TODO  回文失败 断开连接后逻辑处理
            if (isDeveloper())
                onConnBleNextTask(resultData.isResult(), resultData.getMsg());
            else onConnBleNextTask(resultData.isResult(), resultData.getErrorRes());
        }
    }


    @Override
    public void gotoBleSettingsPage() {
        mBleEventPersenter.gotoBluetoothSettingsPage();
    }

    @Override
    public void checkLocationService() {
        mBleEventPersenter.checkLocationService();
    }

    /**
     * 1.超时任务
     * 2.解析BLE回文消息失败
     * 3.获取BLE远程回文消息失败
     *
     * @param msgResId 异常提示Res
     */
    @Override
    public void doErrorEventTask(int msgResId) {
        makeToast(msgResId);
        disConnAndClearBleGatt();
    }

    private void makeToast(int msgResId) {
        if (isDeveloper())
            HandlerUtils.postTask(() -> makeToast(UIUtils.getString(msgResId)));
        else
            HandlerUtils.postTask(() -> makeToast(UIUtils.getString(R.string.base_operate_failed)));
    }


    /**
     * 断开手机BLE所有连接 并且Clear所有回调Callback
     * <p>
     * 1.BEL连接并开锁成功后，清空手机BLE状态;
     * 2.BLE连接超时失败；
     * 3.BLE通信中失败；
     * </p>
     */
    @Override
    public void disConnAndClearBleGatt() {
        BleClent.getInstance().disConnAndClearBleGatt();
    }

    @Override
    public boolean isBleReady() {
        return BleClent.getInstance().isReady();
    }

    @Override
    public void resetBleState() {
        BleClent.getInstance().doResetBleTask();
    }

    @Override
    public boolean isBleEnabled() {
        return BleClent.getInstance().isEnabled();
    }

    /**
     * 连接远程Ble 连接状态回调Callback
     */
    public BleConnCallback connCallback = new BleConnCallback() {

        @Override
        public void startSendMsg(BluetoothGatt gatt) {
            sendMsgToBle(BleEventType, bleMsgCallback);
        }

        @Override
        public void onConnFailure(int code, int msg) {
            super.onConnFailure(code, msg);
            makeToast(msg);
        }

        @Override
        public void disConnDevice() {
            super.disConnDevice();
            makeToast(R.string.base_conn_disconnect);
        }
    };

    /**
     * 解析BLE回文消息体回调Callback
     */
    private ParseBleMsgCallback parseMsgback = new ParseBleMsgCallback() {
        @Override
        public void onError(String errorMsg) {
            doErrorEventTask(R.string.base_ble_return_msg_parse_fail);
        }

        @Override
        public void onWholeMsg(byte[] result) {
            handleBleResponeMsg(result);
        }
    };

    /**
     * BLE远程回文消息回调Callback
     */
    private BleMsgCallback bleMsgCallback = new BleMsgCallback() {

        @Override
        public void receiverRemoteBleMsg(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] value = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                value = characteristic.getValue();
            }
            if (value != null) {
                BleMsgUtlis.readWholeMsg(value, parseMsgback);
            } else {
                doErrorEventTask(R.string.base_ble_return_msg_read_fail);
            }
        }

        @Override
        public void sendMsgError(String errorMsg, int errorRes) {
            doErrorEventTask(errorRes);
        }
    };


    @Override
    public void makeToast(String tipMsg) {
        mBleEventPersenter.makeToast(tipMsg);
    }

    public boolean isDeveloper() {
        return mBleEventPersenter.isTestModle();
    }

    private boolean isNotRepeatClick() {
        if ((System.currentTimeMillis() - LAST_CLICK_TIME) > IBleContract.CLICK_ONPEN_INTERVAL) {
            LAST_CLICK_TIME = System.currentTimeMillis();
            return true;
        } else {
            //   LAST_CLICK_TIME = System.currentTimeMillis();
            makeToast(UIUtils.getString(R.string.base_ble_operate_more));
            return false;
        }
    }


    /**
     * 扫描后直接连接第一个蓝牙操作类型
     *
     * @param bleEventType
     * @param bleDevice
     */
    private void onConnEvent(int bleEventType, BluetoothDevice bleDevice) {
        switch (bleEventType) {
            case BleEvent.EVENT_ENCRYPT /*IBleEventModel.EVENT_ENCRYPT_KEY_TYPE*/:
                onConnBle(bleDevice, bleEventType, connCallback);
                break;
            case BleEvent.EVENT_CHECK_LINK /*IBleEventModel.EVENT_CHECK_LINK_TYPE*/:
                // TODO
                break;
            case BleEvent.EVENT_SET_BLEINFO /*IBleEventModel.EVENT_SET_BLEINFO_TYPE*/:
                // TODO
                //  onConnBle(bleDevice, bleEventType, true, connCallback);
                break;
        }
    }

    @Override
    public boolean isScanConnEvent(@BleEvent int mBleEventType) {
        return (mBleEventType != BleEvent.EVENT_ONLY_SCAN/*IBleEventModel.EVENT_ONLY_SCAN_TYPE*/) && (mBleEventType != BleEvent.EVENT_IDLE/*.EVENT_IDLE_KEY_TYPE*/);
    }

    private boolean isScanConnType(int bleType) {
        return (bleType != BleEvent.EVENT_ONLY_SCAN/*IBleEventModel.EVENT_ONLY_SCAN_TYPE*/) && (bleType != BleEvent.EVENT_IDLE/*.EVENT_IDLE_KEY_TYPE*/);
    }

    /**
     * 蓝牙名称有效性Check
     * <p>
     * 长度	unsigned char	1	最大18字节
     * 蓝牙模块名称	unsigned char	16	蓝牙模块名称，中文只支持UTF-8。
     * </p>
     *
     * @param bleName
     * @return
     */
    @Override
    public boolean isValidName(String bleName) {
        if (StringUtils.isEmpty(bleName)) {
            mBleEventPersenter.makeToast(UIUtils.getString(R.string.base_ble_name_is_not_empty));
            return false;
        }
        byte[] nameBytes = null;
        try {
            nameBytes = bleName.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            mBleEventPersenter.makeToast(e.getMessage());
            return false;
        }
        if (nameBytes == null || nameBytes.length > 18) {
            mBleEventPersenter.makeToast(UIUtils.getString(R.string.base_ble_name_out_len));
            return false;
        }
        return true;
    }
}
