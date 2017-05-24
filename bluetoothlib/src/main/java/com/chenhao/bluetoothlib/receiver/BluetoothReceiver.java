package com.chenhao.bluetoothlib.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.chenhao.bluetoothlib.btinterface.BluetoothStatusListener;


/**
 * Created by chenhao on 2017/4/15.
 * 蓝牙广播接收器
 */

public class BluetoothReceiver extends BroadcastReceiver {
    //蓝牙扫描开始
    public static final int BLUETOOTH_DISCOVERY_STARTED = 0x01;
    //蓝牙扫描结束
    public static final int BLUETOOTH_DISCOVERY_FINISHED = 0x02;
    //找到蓝牙，调用多次
    public static final int BLUETOOTH_ACTION_FOUND = 0x03;
    public static final int BLUETOOTH_OPEN_SUCCESS = 0x04;
    public static final int BLUETOOTH_CLOSE_SUCCESS = 0x05;
    public static final int BLUETOOTH_CONNECTED = 0x06;
    public static final int BLUETOOTH_DISCONNECTED = 0x07;

    public BluetoothReceiver() {
    }
    //蓝牙监听

    public BluetoothReceiver(BluetoothStatusListener listener) {
        this.listener = listener;
    }

    private BluetoothStatusListener listener;

    public void removeBluetoothStatusListener() {
        if (listener != null) {
            listener = null;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                Log.d("BluetoothReceiver", "扫描开始");
                handleBLuetoothStatus(BLUETOOTH_DISCOVERY_STARTED, null);
                break;
            case BluetoothDevice.ACTION_FOUND://每收到一个蓝牙就会调用
                //获得远程设备
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("BluetoothReceiver", "找到设备了");
                handleBLuetoothStatus(BLUETOOTH_ACTION_FOUND, bluetoothDevice);
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED://全部搜索完成
                Log.d("BluetoothReceiver", "扫描结束");
                handleBLuetoothStatus(BLUETOOTH_DISCOVERY_FINISHED, null);
                break;
            case BluetoothAdapter.ACTION_STATE_CHANGED://蓝牙状态变化
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_ON:
                        Log.d("BluetoothReceiver", "蓝牙打开了");
                        handleBLuetoothStatus(BLUETOOTH_OPEN_SUCCESS, null);
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Log.d("BluetoothReceiver", "蓝牙关闭了");
                        handleBLuetoothStatus(BLUETOOTH_CLOSE_SUCCESS, null);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d("BluetoothReceiver", "蓝牙正在打开");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d("BluetoothReceiver", "蓝牙正在关闭");
                        break;
                    case BluetoothAdapter.ERROR:
                        Log.d("BluetoothReceiver", "蓝牙打开错误");
                        break;
                }
                break;
            case BluetoothDevice.ACTION_ACL_CONNECTED:
                BluetoothDevice connectDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                handleBLuetoothStatus(BLUETOOTH_CONNECTED, connectDevice);
                Log.d("BluetoothReceiver", "连接上了" + connectDevice.getName());
                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECTED:

                BluetoothDevice disconnectDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("BluetoothReceiver", "断开了连接" + disconnectDevice.getName());
                handleBLuetoothStatus(BLUETOOTH_DISCONNECTED, disconnectDevice);
                break;
            case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
                switch (bondState) {
                    case BluetoothDevice.BOND_NONE:
                        Log.d("BluetoothReceiver", "删除配对");
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        Log.d("BluetoothReceiver", "正在配对");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.d("BluetoothReceiver", "配对成功");
                        break;
                }
                break;
        }

    }

    /**
     * 处理蓝牙
     *
     * @param status
     */
    private void handleBLuetoothStatus(int status, BluetoothDevice bluetoothDevice) {
        if (listener == null) {
            return;
        }
        switch (status) {
            case BLUETOOTH_DISCOVERY_STARTED:
                listener.discoverStarted();
                break;
            case BLUETOOTH_DISCOVERY_FINISHED:
                listener.discoverFinshed();
                break;
            case BLUETOOTH_ACTION_FOUND:
                listener.bluetoothFound(bluetoothDevice);
                break;
            case BLUETOOTH_OPEN_SUCCESS:
                listener.bluetoothOpenSuccess();
                break;
            case BLUETOOTH_CLOSE_SUCCESS:
                listener.bluetoothCloseSuccess();
                break;
            case BLUETOOTH_CONNECTED:
                listener.bluetoothConnected(bluetoothDevice);
                break;
            case BLUETOOTH_DISCONNECTED:
                listener.bluetoothDisconnect(bluetoothDevice);
                break;
        }
    }


}


