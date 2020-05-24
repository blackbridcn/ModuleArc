package org.net.websocket.model;

/**
 * Author: yuzzha
 * Date: 2019-07-16 14:59
 * Description: 客户端通过 服务端 获取停车缴费的WebSocket 的Http参数Bean
 * Remark: 这个是直接采用了原来Http中参数和数据格式
 */
public class ParkFeeParamData {
    private String car_or_card_number;
    private int type;//  0 则说明第一个参数为车牌号； 1则说明第一个参数为ic卡的卡号
    private String operatorId;// 运营商id
    private String plate;//"plate":"粤AB4567",
    private boolean abIsStdDB = false;//   "abIsStdDB":false

    public String getCar_or_card_number() {
        return car_or_card_number;
    }

    public void setCar_or_card_number(String car_or_card_number) {
        this.car_or_card_number = car_or_card_number;
        setPlate(car_or_card_number);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public boolean isAbIsStdDB() {
        return abIsStdDB;
    }

    public void setAbIsStdDB(boolean abIsStdDB) {
        this.abIsStdDB = abIsStdDB;
    }

}
