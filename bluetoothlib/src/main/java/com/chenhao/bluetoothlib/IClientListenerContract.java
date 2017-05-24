package com.chenhao.bluetoothlib;

import android.bluetooth.BluetoothDevice;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhao on 2017/5/20.
 * 用户需要实现的接口管理
 */

public class IClientListenerContract {
    /**
     * 寻找设备相关
     */
    public interface ISearchDeviceListener {
        void onSearchStart();

        void onFindDevice(BluetoothDevice bluetoothDevice);

        void onSearchEnd(List<BluetoothDevice> bluetoothDevices);
    }

    /**
     * 连接相关
     */
    public interface IConnectListener {
        void onConnectSuccess(BluetoothDevice bluetoothDevice);

        void onConnectFailure(String msg);
    }

    /**
     * 数据接收相关
     */
    public interface IDataReceiveListener {
        void onDataSuccess(byte[] data, int length);

        void onDataFailure(String msg);
    }

    /**
     * 数据发送监听
     */
    public interface IDataSendListener {
        void onDataSendSuccess(byte[] data);

        void onDataSendFailure(String msg);
    }


    /**
     * 蓝牙打开成功或关闭
     */
    public interface IBlueClientIsOpenListener {
        void onOpen();

        void onClose();
    }

    /**
     * 蓝牙全局状态监听
     */
    public interface IBluetoothStatusListener {
        void discoverStart();

        void discoverEnd(ArrayList<BluetoothDevice> mBlueDevices);

        void bluetoothFound(BluetoothDevice bluetoothDevice);

        void bluetoothOpen();

        void bluetoothClose();

        void bluetoothConnected(BluetoothDevice bluetoothDevice);

        void bluetoothDisconnect(BluetoothDevice bluetoothDevice);
    }
}
