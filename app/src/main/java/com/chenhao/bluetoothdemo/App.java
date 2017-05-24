package com.chenhao.bluetoothdemo;

import android.app.Application;

import com.chenhao.bluetoothlib.BluetoothClient;

/**
 * Created by chenhao on 2017/5/21.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BluetoothClient.init(this);
    }
}
