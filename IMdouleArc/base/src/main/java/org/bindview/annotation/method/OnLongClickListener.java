package org.bindview.annotation.method;

import android.view.View;

import androidx.annotation.IdRes;

import org.bindview.annotation.internal.MethodEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@MethodEvent(registerCallbaceLisenerApiMethodName = "setOnLongClickListener", onLisenerMethodCallbackMehod = "onLongClick", callbackListenerInterfaceParams = {View.OnLongClickListener.class})
public @interface OnLongClickListener {
    @IdRes int[] value() default {View.NO_ID};
}
