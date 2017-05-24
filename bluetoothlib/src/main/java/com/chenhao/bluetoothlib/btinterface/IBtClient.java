package com.chenhao.bluetoothlib.btinterface;

import com.chenhao.bluetoothlib.IClientListenerContract;

/**
 * Created by chenhao on 2017/5/19.
 * 蓝牙模块所具备的方法
 */

public interface IBtClient {
    /**
     * 绑定当前模块
     *
     * @param iCommonBTModule
     */
    void bindBlueModule(ICommonBTModule iCommonBTModule);

    void searchBluetoothDevices(IClientListenerContract.ISearchDeviceListener onSearchDeviceListener);

    void connectDevice(String address, IClientListenerContract.IConnectListener iConnectListener);

    void openBluetooth(IClientListenerContract.IBlueClientIsOpenListener iBlueClientIsOpenListener);

    void closeBluetooth(IClientListenerContract.IBlueClientIsOpenListener iBlueClientIsOpenListener);

    void sendMessage(String address, byte[] data);

    void recevieMessage(IClientListenerContract.IDataReceiveListener iDataReceiveListener);

    void onDestroy();//停止蓝牙
}
