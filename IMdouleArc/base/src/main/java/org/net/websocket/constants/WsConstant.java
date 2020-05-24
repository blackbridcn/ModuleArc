package org.net.websocket.constants;

/**
 * Author: yuzzha
 * Date: 2019-07-15 14:40
 * Description: WebSocket SDK 中的一下常量
 * Remark:
 */
public interface WsConstant {

    //String PeakeWsUrl = "ws://192.168.1.40:1234?clientType=ANDROID";
    String PeakeWsUrl = "ws://test.peake.com.cn:1234?clientType=ANDROID";

    //WS 消息类型
    interface MsgType {
        String MSG_NOTICE_TYPE = "NOTICE"; //result 为false 服务端返回Wx消息失败时提示信息类型
        String MSG_INIT_TYPE = "INIT_INFO";//WS初始化
        String MSG_PARKFEE_TYPE = "PARKING_FEE";//停车缴费
        String MSG_HEART_TYPE = "HEARTBEAT";//心跳包
    }

    //WS 连接状态
    interface ConnStatus {
        int CONN_DIS_CONN = 0;//已经断开WS连接
        int CONN_DIS_ING = 1;//正在断开WS连接
        int CONN_CONN_ING = 2;//正在连接
        int CONN_CONN_OK = 3;//已经连接
        int CONN_RETRY_CONN = 4;//正在重新连接
    }

    //
    interface CODE {
        int NORMAL_CLOSE = 1000;
        int ABNORMAL_CLOSE = 1001;
    }

    //WebSocket 中的一下提示Tip
    interface TIP {
        String NORMAL_CLOSE = "normal close";
        String ABNORMAL_CLOSE = "abnormal close";
    }
    //WebSocket 网络状态Statue
    interface NET_STATUS {
        int NETWORK_NONE = -1;
        int NETWORK_WIFI = 0;
        int NETWORK_MOBILE = 1;
    }
}
