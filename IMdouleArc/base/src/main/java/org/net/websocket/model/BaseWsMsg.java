package org.net.websocket.model;

/**
 * Author: yuzzha
 * Date: 2019-07-15 15:13
 * Description:  WebSocket 服务端返回Msg BaseJson
 * Remark:
 */
public class BaseWsMsg {

    /**
     * type : INIT_INFO
     * data : {"clientId":"e454e8fffe52e18e-00004af8-00001754-2ca707bda3e2be6a-3e82a590"}
     * timestamp : 1563174747767
     * result : true
     */

    private String type;
    private String data;
    private long timestamp;
    private boolean result;
    //result 为false时才会有msg字段
    private String msg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
