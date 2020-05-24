package org.imodule.help.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: yuzzha
 * Date: 2019/5/10 16:52
 * Description:  各个模块中引用/调用其他第三方Module 变量的注解
 * Remark:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Module {
    String value();
}
