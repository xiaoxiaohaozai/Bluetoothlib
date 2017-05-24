package com.chenhao.bluetoothlib.btinterface;

import com.chenhao.bluetoothlib.IClientListenerContract;
import com.chenhao.bluetoothlib.entity.TypeMessage;

/**
 * Created by chenhao on 2017/5/19.
 * 蓝牙模块，经典蓝牙，低功耗蓝牙，实现该接口
 */

public interface ICommonBTModule {
    void searchBtDevices(IClientListenerContract.ISearchDeviceListener onSearchDeviceListener);

    void connectBt(String address, IClientListenerContract.IConnectListener iConnectListener);

    void openBt(IClientListenerContract.IBlueClientIsOpenListener iBlueClientIsOpenListener);

    void closeBt(IClientListenerContract.IBlueClientIsOpenListener iBlueClientIsOpenListener);

    void sendMsg(String address, byte[] data);

    void readMes(IClientListenerContract.IDataReceiveListener iDataReceiveListener);

    void disconnectBt();

    void addBlueStatusListener(IClientListenerContract.IBluetoothStatusListener iBluetoothStatusListener);

    void removeBlueStatusListener(IClientListenerContract.IBluetoothStatusListener iBluetoothStatusListener);

    void onDestory();

    boolean currentBtEnable();
}
