/**
 * Simple to Introduction
 *
 * @ProjectName: [PeakeCloud]
 * @Package: [com.poobo.sms.EnumSmsType.java]
 * @ClassName: [EnumSmsType]
 * @Description: [一句话描述该类的功能]
 * @Author: [lei.ma]
 * @CreateDate: [2016年6月3日 上午10:12:18]
 * @UpdateUser: [RayMa]
 * @UpdateDate: [2016年6月3日 上午10:12:18]
 * @UpdateRemark: [说明本次修改内容]
 * @Version: [v1.0]
 */
package com.base;

/**
 * 短信验证码类型
 *
 */
public enum BaseSmsCode {

    not_exits(-1, "不存在的"),
    alert_login_pwd(0, "修改登录密码"),
    alert_auth(1, "认证信息修改"),
    alert_email(2, "修改邮箱"),
    set_operate_pwd(3, "设置操作密码"),
    register_code(4, "注册验证码"),
    change_phone(5, "更改手机号"),
    door_secret(6, "门禁短信"),
    door_secret_forget(7, "门禁密保重置"),
    reset_device_no(8,"重置设备号"),
    time_out(-997, "用户登录超时或别处登录");

    private int value;
    private String desc;


    private BaseSmsCode(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static BaseSmsCode setValue(int str) {

        BaseSmsCode[] list = BaseSmsCode.values();
        for (BaseSmsCode enums : list) {
            if (enums.getValue() == str) {
                return enums;
            }
        }

        return not_exits;

    }


}
