package com.chenhao.bluetoothlib.btinterface;

/**
 * Created by chenhao on 2017/5/19.
 * 蓝牙模块，经典蓝牙，低功耗蓝牙，实现该接口
 */

public interface ICommonBTModule {
    void searchBtDevices(IClientListenerContract.ISearchDeviceListener onSearchDeviceListener);

    void connectBt(String address, IClientListenerContract.IConnectListener iConnectListener);

    void openBt(IClientListenerContract.IBlueClientIsOpenListener iBlueClientIsOpenListener);

    void closeBt(IClientListenerContract.IBlueClientIsOpenListener iBlueClientIsOpenListener);

    void sendMsg(byte[] data, IClientListenerContract.IDataSendListener dataSendListener);

    void readMes(IClientListenerContract.IDataReceiveListener iDataReceiveListener);


    void addBlueStatusListener(IClientListenerContract.IBluetoothStatusListener iBluetoothStatusListener);

    void removeBlueStatusListener(IClientListenerContract.IBluetoothStatusListener iBluetoothStatusListener);

    void onDestory();

    boolean currentBtEnable();

    void cancelBtSearch();

    String getLocalName();

    void openServer(IClientListenerContract.IServerStatusListener iServerStatusListener);
}
