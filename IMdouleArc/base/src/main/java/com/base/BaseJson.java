package com.base;

import android.text.TextUtils;

public class BaseJson {
    public BaseJson() {
    }


    /**
     * errorCode :
     * resultCode : SUCCESS
     * resultData : {"login_date":"1219653418","session_id":"100869","time_out":3000,"user_id":"PCX","wechat_server":" "}
     * resultMsg : 成功
     * returnCode : SUCCESS
     * returnMsg : 登录成功
     */

    private boolean returnCode;
    private String returnMsg;
    private boolean resultCode;
    private String resultMsg;
    private Object resultData;
    private int errorCode;
    private String errorMsg;

    public boolean getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        if (!TextUtils.isEmpty(returnCode)) {
            if ("SUCCESS".equals(returnCode.trim())) {
                this.returnCode = true;
            } else {
                this.returnCode = false;
            }
        } else {
            this.returnCode = false;
        }

    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public boolean getResultCode() {
        return resultCode && returnCode;
    }

    public void setResultCode(String resultCode) {
        if (!TextUtils.isEmpty(resultCode)) {
            if ("SUCCESS".equals(resultCode.trim())) {
                this.resultCode = true;
            } else this.resultCode = false;
        } else this.resultCode = false;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "BaseJson{" +
                "returnCode='" + returnCode + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                ", resultData=" + resultData +
                ", errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
