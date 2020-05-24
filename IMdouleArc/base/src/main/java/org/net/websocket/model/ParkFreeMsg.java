package org.net.websocket.model;

import java.util.UUID;

/**
 * Author: yuzzha
 * Date: 2019-07-16 11:00
 * Description: 客户端向服务端发送获取停车缴费的请求Msg
 * Remark:
 */
public class ParkFreeMsg {

    /**
     * id : 消息唯一UUID（可选）
     * type : PARKING_FEE
     * from  : clientId
     * fromType  : ANDROID
     * to  : 运营商id
     * toType : YKT
     * data : {}
     */
    private String id;
    private String type;
    private String from;
    private String fromType;
    private String to;
    private String toType;
    private Object data;

    public ParkFreeMsg() {
    }

    public ParkFreeMsg(String from, String type, String to, Object data) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.from = from;
        this.fromType = "ANDROID";
        this.to = to;
        this.toType = "YKT";
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
