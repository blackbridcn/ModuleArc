package org.bluetooth.classic;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import org.bluetooth.ble.core.config.BluethConfig;


public class ClassicManager  {

    private Context mContext;
    private BluetoothAdapter bluetoothAdapter;
    private BluethConfig connConfig;

    private ClassicManager() {
    }

    private final static class ClassicManagerHolder {
        private static final ClassicManager Instance = new ClassicManager();
    }

    public static ClassicManager getInstance() {
        return ClassicManagerHolder.Instance;
    }


}
