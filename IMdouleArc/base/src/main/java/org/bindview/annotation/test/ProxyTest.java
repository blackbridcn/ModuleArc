package org.bindview.annotation.test;

import android.view.View;

import androidx.annotation.IdRes;

import org.bindview.annotation.internal.MethodEvent;
import org.bindview.demo.TestView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@MethodEvent(registerCallbaceLisenerApiMethodName = "setProxyTestLinsener", onLisenerMethodCallbackMehod = "onProxyTest", callbackListenerInterfaceParams = {TestView.ProxyTestListener.class})
public @interface ProxyTest {

    @IdRes int[] value() default {View.NO_ID};
}
