package org.perminssion;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.reactivex.internal.functions.ObjectHelper;

public class RunnTimePreminssion {

    @SuppressLint("CheckResult")
    public static void requestEach(@NotNull Activity mActivity, @Nullable PremequestResultLisenter lisenter, @NotNull String... permissions) {
        ObjectHelper.requireNonNull(mActivity, "Activity is null");
        ObjectHelper.requireNonNull(permissions, "Request  RunnTime Permissions is null");
        RxPermissions rxPermissions = new RxPermissions(mActivity);//TRIGGER
        rxPermissions.requestEach(permissions).subscribe((permission) -> {
            if (lisenter != null) {
                lisenter.requestResult(permission.name, permission.granted, permission.shouldShowRequestPermissionRationale);
            }
        });
    }



    public  interface PremequestResultLisenter {
        /**
         * @param permission   请求权限
         * @param granted      是否获取权限
         * @param requestAgain 拒绝后是否允许再次申请
         */
        void  requestResult(String permission, boolean granted, boolean requestAgain);

    }




}
