package org.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * 判断网络状态
 *
 */
public class NetworkUtils {

    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context){
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(state == State.CONNECTED||state == State.CONNECTING){
            return NETWORN_WIFI;
        }
        //3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if(state == State.CONNECTED||state == State.CONNECTING){
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }    
    
	/**
	 * 检测网路
	 * 
	 * @param context
	 * @return
	 */

	public static boolean isNetWorkAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}

	/**
	 * 获取当前网络连接状态
	 *
	 * @param context
	 * @return boolean
	 */
	public static boolean checkNetworkInfo(Context context) {
		try {
			ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobileNetInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			State mobile = null, wifi = null;
			if (mobileNetInfo != null)
				// 获取3G网络状态
				mobile = mobileNetInfo.getState();
			if (wifiNetInfo != null)
				// 获取wifi网络状态
				wifi = wifiNetInfo.getState();

			// 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
			if (mobile == State.CONNECTED || mobile == State.CONNECTING)
				return true;
			if (wifi == State.CONNECTED || wifi == State.CONNECTING)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
	
	/**
	 * 判断WIFI网络是否可用
	 * 
	 * @param context
	 * @return
	 */

	public static boolean checkNetworkConnection(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (State.CONNECTED == state) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断MOBILE网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isMobileDataEnable(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isMobileDataEnable = false;
		isMobileDataEnable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		return isMobileDataEnable;
	}

	/**
	 * 判断wifi 是否可用
	 * @param context
	 * @return
	 */
	public static boolean isWifiDataEnable(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isWifiDataEnable = false;
		isWifiDataEnable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		return isWifiDataEnable;
	}

	//判断是否打开了开发者模式
	//boolean enableAdb = (Settings.Secure.getInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 0) > 0);
	//

	//判断 蓝牙是否打开
	public static boolean isOpenBluetooth(){
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		return adapter.isEnabled();
	}
}
