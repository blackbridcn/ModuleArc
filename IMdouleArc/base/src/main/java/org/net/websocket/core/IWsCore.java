package org.net.websocket.core;

import org.net.websocket.model.BaseWsMsg;

import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Author: yuzzha
 * Date: 2019-07-15 16:32
 * Description: WebSocket 操作Core interface
 * Remark:
 */
public interface IWsCore {

    /**
     * 获取WebSocket实例
     *
     * @return WebSocket实例
     */
    WebSocket getWebSocket();

    /**
     * 开始建立连接
     */
    void onBuildConnect();

    /**
     * 断开连接
     */
    void stopConnect();

    /**
     * 当前WebSocket是否已经连接
     *
     * @return 连接状态 true 表示已经连接,fase表示断开连接;
     */
    boolean isWsConnected();

    /**
     * 获取当前连接的ClineId
     *
     * @return ClintId
     */
    String getClintId();

    /**
     * 发送WebSocket  String类型消息Msg
     *
     * @param msg Msg消息
     * @return 客户端是否结果 true表示发送成功;false 表示发送失败;
     */
    boolean sendMessage(String msg);

    /**
     * 发送WebSocket  ByteString类型消息Msg
     *
     * @param byteString Msg消息
     * @return 客户端是否结果 true表示发送成功;false 表示发送失败;
     */
    boolean sendMessage(ByteString byteString);

    /**
     * 设置服务端返回消息回调监听
     *
     * @param onWsMessageCallback OnWsMessageCallback
     */
    void setOnWsMessageCallback(OnWsMessageCallback onWsMessageCallback);

    //WebSocket远程消息回调监听interface
    interface OnWsMessageCallback {
        /**
         * WebSocket 服务端返回远程消息 ；
         * <p>
         * 备注：WebSocket初始化消息[ WsConstan.MsgType.MSG_HEART_TYPE ] 在内部进行了拦截，没有向外回调发出;
         *
         * @param data BaseWsMsg
         */
        void onReciverMessage(BaseWsMsg data);
    }

    //WebSocket网络状态Change回调监听interface
    interface OnNetStatueChangeListener {
        void onNetStatue(boolean hasNet, int statue);
    }
}
