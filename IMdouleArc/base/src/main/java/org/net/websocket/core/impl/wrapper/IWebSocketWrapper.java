package org.net.websocket.core.impl.wrapper;

import org.net.websocket.model.BaseWsMsg;

import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * Author: yuzzha
 * Date: 2019-07-23 14:01
 * Description: WebSocke对象操作Core函数
 * Remark:
 */
public interface IWebSocketWrapper {

    /**
     * Handler 消息转发到主线程函数
     *
     * @param task Runnable
     */
    void doDispatchMsgMainThreadTask(Runnable task);

    /**
     * Handler 的 postDelayed(delayTask, delayMillis)函数
     *
     * @param delayTask   Runnable
     * @param delayMillis 延时时间
     */
    void doHandlerDelayedTask(Runnable delayTask, long delayMillis);

    /**
     * Handler 的  removeCallbacks(Runnable r)
     *
     * @param delayTask Runnable
     */
    void doRemoveHandlerDelayTask(Runnable delayTask);


    /**
     * 设置WS连接状态
     *
     * @param mConnStatus @ link${WsConstant.ConnStatus}
     */
    void setWsConnStatus(int mConnStatus);

    /**
     * 判断是否WS已经连接
     *
     * @return
     */
    boolean isWsConnected();

    /**
     * 是否已经断开
     *
     * @return
     */
    boolean isDisConn();

    /**
     * 获取本次连接ClinetId
     *
     * @return clientId
     */
    String getClinetId();

    /**
     * 获取当前连接WebSocket对象
     *
     * @return WebSocket
     */
    WebSocket getWebSocket();

    /**
     * 手动释放当前WebSocket 连接和持有对象
     */
    void doManualReleaseTask();

    /**
     * 只是发送操作,调用函数之前需验证是否连接
     *
     * @param wsMsg 需要通过WS发送的Message ;
     * @return 发送的 Message 结果
     */
    boolean sendWsMsgMethod(Object wsMsg);

    /**
     * 设置WS关闭监听
     *
     * @param onCloseWsLinsenter OnCloseWsLinsenter
     */
    void setOnCloseWsLinsenter(OnCloseWsLinsenter onCloseWsLinsenter);

    /**
     * 设置WS连接监听
     *
     * @param onOpenWsLinsener OnOpenWsLinsener
     */
    void setOnOpenWsLinsener(OnOpenWsLinsener onOpenWsLinsener);

    /**
     * 设置WS回文消息监听
     *
     * @param onReciveWsMsgLinsener  OnReciveWsMsgLinsener
     */
    void setOnReciveWsMsgLinsener(OnReciveWsMsgLinsener onReciveWsMsgLinsener);

    /**
     * 设置连接失败监听
     *
     * @param onFailureWsMsgLinsener OnFailureWsMsgLinsener
     */
    void setOnFailureWsMsgLinsener(OnFailureWsMsgLinsener onFailureWsMsgLinsener);

    interface OnCloseWsLinsenter {
        void onCloseWs(WebSocket webSocket, String reason);
    }

    interface OnOpenWsLinsener {
        void onOpenWs(WebSocket webSocket, Response response);
    }

    interface OnReciveWsMsgLinsener {
        void OnReciveWsMsg(WebSocket webSocket, BaseWsMsg data);
    }

    interface OnFailureWsMsgLinsener {
        void OnFailureWsConn(WebSocket webSocket);
    }
}
