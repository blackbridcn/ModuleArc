package com.base.param;

import android.text.TextUtils;

/**
 * Author: yuzzha
 * Date: 3/9/2020 7:12 PM
 * Description:
 * Remark:
 */
public class BaseRespJson<T extends Object> {

    /**
     * errorCode :
     * resultCode : SUCCESS
     * resultData : {"login_date":"1219653418","session_id":"100869","time_out":3000,"user_id":"PCX","wechat_server":" "}
     * resultMsg : 成功
     * returnCode : SUCCESS
     * returnMsg : 登录成功
     */


    /**
     *
     * {
     *     "errorCode":"",
     *     "errorMsg":"",
     *     "resultCode":"SUCCESS",
     *     "resultData":{
     *         "list":[
     *             {
     *                 "picPath":"http://120.76.47.240:8888/group1/M00/00/27/eEwv8FpTKpOAX2DDAAvqH_kipG8781.jpg",
     *                 "pkGlobalId":"059cf8c9-ad5d-40fc-827e-dc80e67064d7",
     *                 "startTime":1515399818000,
     *                 "title":"测试2",
     *                 "type":0,
     *                 "url":"http://192.168.1.37:8080/PeakeCloud/mobile/other/toAd?pkId=059cf8c9-ad5d-40fc-827e-dc80e67064d7",
     *                 "urlType":0
     *             },
     *             {
     *                 "picPath":"http://120.76.47.240:8888/group1/M00/00/17/eEwv8FhE0hmAGkCKAA1rIuRd3Es169.jpg",
     *                 "pkGlobalId":"900dfefa-514e-41ed-a0fa-04573f64d195",
     *                 "startTime":1480905233000,
     *                 "title":"测试上传",
     *                 "type":0,
     *                 "url":"https://www.baidu.com/",
     *                 "urlType":1
     *             }
     *         ]
     *     },
     *     "resultMsg":"成功",
     *     "returnCode":"SUCCESS",
     *     "returnMsg":"成功",
     *     "success":true
     * }
     */


    private boolean returnCode;
    private String returnMsg;
    private boolean resultCode;
    private String resultMsg;
    private T resultData;
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

    public T getResultData() {
        return resultData;
    }

    public void setResultData(T resultData) {
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
