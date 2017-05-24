package com.chenhao.bluetoothlib.btinterface;

import android.bluetooth.BluetoothDevice;

/**
 * Created by chenhao on 2017/5/19.
 * 蓝牙广播回调
 */

public interface BluetoothStatusListener {
    void discoverStarted();

    void discoverFinshed();

    void bluetoothFound(BluetoothDevice bluetoothDevice);

    void bluetoothOpenSuccess();

    void bluetoothCloseSuccess();

    void bluetoothConnected(BluetoothDevice bluetoothDevice);

    void bluetoothDisconnect(BluetoothDevice bluetoothDevice);

}
