package com.base;

import androidx.annotation.StringRes;

import com.application.BaseApplication;


public enum BaseNetErrorCode {

    PARAM_ERROR(-999, R.string.base_param_error/*"参数错误"*/),
    PWD_ERROR(-998, R.string.base_password_error/*"用户名密码错误"*/),
    LOGIN_TIME_OUT_ERROR(-997, R.string.base_password_error/*"用户登录超时"*/),
    LOGIN_OTHER_ERROR(-996, R.string.base_login_other_error/*"用户在别处登录"*/),
    MEMBER_FROZEN_ERROR(-995, R.string.base_member_frozen_error/*"会员冻结"*/),
    MEMBER_NOT_ACTIVATE_ERROR(-994, R.string.base_member_not_activate_error/*"会员未激活"*/),
    DEVICE_NUM_ERROR(-993, R.string.base_device_number_error/*"设备码不一致"*/),

    pay_type_not_exits(-899, R.string.base_pay_type_error/*"不存在的支付方式"*/),
    pay_payed(-898, R.string.base_pay_payed_error/*"已支付"*/),
    pay_zero(-897, R.string.base_pay_zero_error/*"支付金额为0"*/),
    wallet_not_exits(-896, R.string.base_wallet_not_exit_error/*"钱包不存在"*/),
    wechat_reload(-895, R.string.base_wechat_reload_error/*"需要微信重定向"*/),

    comp_not_exits(-799, R.string.base_comp_not_exits_error/*"不存在的公司"*/),
    YKT_CONNECT_ERROR(-798, R.string.base_ykt_connect_error/*"一卡通链接错误"*/),
    YKT_DEAL_ERROR(-797, R.string.base_ykt_deal_error/*"一卡通处理错误"*/),
    YKT_OFFLINE(-796, R.string.base_ykt_offline_error/*"一卡通离线"*/),
    OTHER_ERROR(-1, R.string.base_other_error/*"其他错误"*/),
    LOCAL_JSON_COVERT_ERROR(-2, R.string.base_cover_data_error/*"解析服务端JSON数据错误"*/);


    private int value;

    private int desc;

    BaseNetErrorCode(int value, int desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public int getErrorDesc() {
        return desc;
    }

    public static BaseNetErrorCode setValue(int str) {
        for (BaseNetErrorCode e : BaseNetErrorCode.values()) {
            if (e.getValue() == str)
                return e;
        }
        return PARAM_ERROR;
    }

    public static String getErrorDesc(int value) {
        for (BaseNetErrorCode e : BaseNetErrorCode.values()) {
            if (e.getValue() == value)
                return  getResString(e.getErrorDesc());
        }
        return  getResString(OTHER_ERROR.getErrorDesc());
    }

    public String getDesc() {
        return this.getResString(this.desc);
    }

    private static String getResString(@StringRes int resId) {
        return BaseApplication.getContext().getResources().getString(resId);
    }

}
