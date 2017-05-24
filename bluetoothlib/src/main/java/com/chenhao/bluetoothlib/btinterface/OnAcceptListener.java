package com.chenhao.bluetoothlib.btinterface;

import android.bluetooth.BluetoothSocket;

import com.chenhao.bluetoothlib.thread.DataHandleThread;

/**
 * Created by chenhao on 2017/5/24.
 * 服务器相关监听
 */

public interface OnAcceptListener {

    void onGetClientSuccess(BluetoothSocket socket);

    void onGetClientFailure(String message);

}
