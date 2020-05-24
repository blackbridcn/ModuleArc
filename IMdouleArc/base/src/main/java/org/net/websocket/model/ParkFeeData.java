package org.net.websocket.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: yuzzha
 * Date: 2019-07-24 18:00
 * Description: 服务商获取停车缴费具体信息Bean
 * Remark:
 */
public class ParkFeeData implements Parcelable {

    /**
     * totalAmount : 53  总计停车费用
     * discountCodeMsg : null
     * entryTime : 2019-07-22 14:07:36  入场时间
     * deductionAmount : 0
     * In_LogID : 1
     * unPayAmount : 53 未付停车缴费金额
     * feeId : 2019072400083  record_id
     */

    private double totalAmount;
    private Object discountCodeMsg;
    private String entryTime;
    private int deductionAmount;
    private int In_LogID;
    private double unPayAmount;
    private String feeId;

    private String attach;

    public ParkFeeData() {
    }


    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Object getDiscountCodeMsg() {
        return discountCodeMsg;
    }

    public void setDiscountCodeMsg(Object discountCodeMsg) {
        this.discountCodeMsg = discountCodeMsg;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public int getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(int deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public int getIn_LogID() {
        return In_LogID;
    }

    public void setIn_LogID(int In_LogID) {
        this.In_LogID = In_LogID;
    }

    public double getUnPayAmount() {
        return unPayAmount;
    }

    public void setUnPayAmount(double unPayAmount) {
        this.unPayAmount = unPayAmount;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    protected ParkFeeData(Parcel in) {
        totalAmount = in.readInt();
        entryTime = in.readString();
        deductionAmount = in.readInt();
        In_LogID = in.readInt();
        unPayAmount = in.readInt();
        feeId = in.readString();
        attach = in.readString();
    }

    public static final Creator<ParkFeeData> CREATOR = new Creator<ParkFeeData>() {
        @Override
        public ParkFeeData createFromParcel(Parcel in) {
            return new ParkFeeData(in);
        }

        @Override
        public ParkFeeData[] newArray(int size) {
            return new ParkFeeData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(totalAmount);
        dest.writeString(entryTime);
        dest.writeInt(deductionAmount);
        dest.writeInt(In_LogID);
        dest.writeDouble(unPayAmount);
        dest.writeString(feeId);
        dest.writeString(attach);
    }

    @Override
    public String toString() {
        return "ParkFeeData{" +
                "totalAmount=" + totalAmount +
                ", discountCodeMsg=" + discountCodeMsg +
                ", entryTime='" + entryTime + '\'' +
                ", deductionAmount=" + deductionAmount +
                ", In_LogID=" + In_LogID +
                ", unPayAmount=" + unPayAmount +
                ", feeId='" + feeId + '\'' +
                ", attach='" + attach + '\'' +
                '}';
    }
}
