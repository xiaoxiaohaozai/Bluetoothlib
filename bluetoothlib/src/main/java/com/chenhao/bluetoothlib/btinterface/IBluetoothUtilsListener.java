package com.chenhao.bluetoothlib.btinterface;


/**
 * Created by chenhao on 2017/5/19.
 * 蓝牙client所具备的方法
 */

public interface IBluetoothUtilsListener {
    /**
     * 绑定蓝牙模块
     *
     * @param iCommonBTModule
     */
    void bindBlueModule(ICommonBTModule iCommonBTModule);

    void searchBluetoothDevices(IClientListenerContract.ISearchDeviceListener onSearchDeviceListener);

    void connectDevice(String address, IClientListenerContract.IConnectListener iConnectListener);

    void openBluetooth(IClientListenerContract.IBlueClientIsOpenListener iBlueClientIsOpenListener);

    void closeBluetooth(IClientListenerContract.IBlueClientIsOpenListener iBlueClientIsOpenListener);

    void sendMessage( byte[] data,IClientListenerContract.IDataSendListener dataSendListener);

    void recevieMessage(IClientListenerContract.IDataReceiveListener iDataReceiveListener);

    void cancelBluetoothSearch();

    String getLocalDeviceName();//本地蓝牙名称

    void onDestroy();//销毁蓝牙实例

    boolean getBluetoothEnable();

}
