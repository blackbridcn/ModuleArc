package org.net.websocket.model;

/**
 * Author: yuzzha
 * Date: 2019-07-23 11:06
 * Description: WebSocket 客户端发给服务端的心跳包JavaBean
 * Remark:
 */
public class HeartMsg {

    private String type = "HEARTBEAT";
    private String clientId;//ClinetId
    private String fromType = "ANDROID";

    public HeartMsg() {
    }

    public HeartMsg(String clientId) {
        this.clientId = clientId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }
}
