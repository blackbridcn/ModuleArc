package org.net.websocket.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.net.websocket.constants.WsConstant;


/**
 * Author: yuzzha
 * Date: 2019-07-23 14:39
 * Description: WebSocket 模块内部网络Utils类
 * Remark:
 */
public class NetUtil {

    //检查网络是否连接
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getNetWorkState(Context context) {
        //得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        //如果网络连接，判断该网络类型
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return WsConstant.NET_STATUS.NETWORK_WIFI;//wifi
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return WsConstant.NET_STATUS.NETWORK_MOBILE;//mobile
            }
        } else {
            //网络异常
            return WsConstant.NET_STATUS.NETWORK_NONE;
        }
        return WsConstant.NET_STATUS.NETWORK_NONE;
    }
}
