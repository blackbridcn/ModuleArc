package org.net.websocket;

import com.alibaba.fastjson.JSON;

import org.net.websocket.constants.WsConstant;
import org.net.websocket.core.IWsCore;
import org.net.websocket.core.impl.WsCoreImpl;
import org.net.websocket.core.impl.config.WsConfig;
import org.net.websocket.model.HeartMsg;
import org.net.websocket.model.ParkFeeParamData;
import org.net.websocket.model.ParkFreeMsg;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: yuzzha
 * Date: 2019-07-15 16:52
 * Description:WebSocket Client 外部直接调用
 * Remark:
 */
public class WsClient {

    private IWsCore iWsCore;
    private Timer time;
    private TimerTask timerTask;
    private boolean result;
    private HeartMsg heartMsg;

    /**
     * 初始化实例入口
     *
     * @param wsConfig WsConfig
     * @return WsClient
     */
    public static WsClient newWsInstance(WsConfig wsConfig) {
        return new WsClient(wsConfig);
    }

    private WsClient(WsConfig wsConfig) {
        iWsCore = new WsCoreImpl(wsConfig);
        iWsCore.onBuildConnect();
        heartMsg = new HeartMsg();
        startHeartTask();
    }

    /**
     * 开始发送心跳包的定时任务Time;
     * 5S 后开始发送，每间隔5S发送一次
     */
    private void startHeartTask() {
        time = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                sendHeartMsg();
            }
        };
        time.scheduleAtFixedRate(timerTask, 5 * 1000, 5 * 1000);
    }

    /**
     * 取消定时心跳任务
     */
    private void cancelTimerTask() {
        timerTask.cancel();
        time.cancel();
    }

    /**
     * 设置WedSocket回文消息回调监听
     *
     * @param onWsMessageCallback IWsCore.OnWsMessageCallback
     */
    public void setWsMessageCallback(IWsCore.OnWsMessageCallback onWsMessageCallback) {
        if (onWsMessageCallback != null)
            iWsCore.setOnWsMessageCallback(onWsMessageCallback);
    }

    /**
     * 定时发送Http心跳包
     * 5S 发送一次
     */
    private void sendHeartMsg() {
        if (iWsCore.isWsConnected() && !result) {
            heartMsg.setClientId(iWsCore.getClintId());
            iWsCore.sendMessage(JSON.toJSONString(heartMsg));
        } else {
            result = false;
        }
    }

    /**
     * 获取停车缴费的信息
     *
     * @param operateId 运营商Id
     * @param data      ParkFeeParamData 车牌信息JavaBean
     * @return WebSocket客户端是否发送成功
     */
    public boolean sendGetPakeFreeMsg(String operateId, ParkFeeParamData data) {
        ParkFreeMsg freeMsg = new ParkFreeMsg(iWsCore.getClintId(), WsConstant.MsgType.MSG_PARKFEE_TYPE, operateId, data);
        String msg = JSON.toJSONString(freeMsg);
        result = iWsCore.sendMessage(msg);
        return result;
    }

    /**
     * WebSocket 操作结束 实例对象释放函数
     */
    public void onRelease() {
        cancelTimerTask();
        iWsCore.stopConnect();
    }

}
