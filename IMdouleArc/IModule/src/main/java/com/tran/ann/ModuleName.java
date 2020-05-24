package com.tran.ann;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: yuzzha
 * Date: 2019-09-06 9:31
 * Description: 完整Apk的模块中的名 注解
 * Remark:该注解用于模块管理类{ @ModuleHandler.class} 中函数的 参数约束
 */
@StringDef(value = {ModuleName.MODUEL_LOGIN, ModuleName.MODUEL_MINE, ModuleName.MODUEL_FEE,
        ModuleName.MODUEL_OTHER})
@Retention(RetentionPolicy.SOURCE)
public @interface ModuleName {

    String MODUEL_LOGIN = "LoginModuleImpl";

    String MODUEL_MINE = "MineModuleImpl";

    String MODUEL_FEE = "FeeModuleImpl";


    String MODUEL_OTHER = "OtherModuleImpl";

}
