package com.chenhao.bluetoothdemo;

import android.app.Application;
import com.chenhao.bluetoothlib.BluetoothUtils;

/**
 * Created by chenhao on 2017/5/21.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BluetoothUtils.init(this);
    }
}
