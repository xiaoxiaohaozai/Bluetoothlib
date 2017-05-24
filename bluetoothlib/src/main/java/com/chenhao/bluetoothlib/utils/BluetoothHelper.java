package com.chenhao.bluetoothlib.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.Set;


/**
 * Created by chenhao on 2017/4/28.
 * 蓝牙工具类
 */

public class BluetoothHelper {
    private static BluetoothHelper instance;
    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;

    /**
     * 获取BluetoothHelper实例，采用Application剥离Activity生命周期
     *
     * @param context
     * @return BluetoothHelper
     */
    public static BluetoothHelper getInstance(Context context) {

        if (instance == null) {
            synchronized (BluetoothHelper.class) {
                if (instance == null) {
                    instance = new BluetoothHelper(context);
                }
            }
        }

        return instance;
    }

    /**
     * 私有构造函数
     *
     * @param context
     * @hide
     */
    private BluetoothHelper(Context context) {
        this.mContext = context;
        mBluetoothAdapter = getAdapter();
    }

    /***
     * 获取BluetoothAdapter
     *
     * @return BluetoothAdapter
     * @hide
     */
    private BluetoothAdapter getAdapter() {
        BluetoothAdapter mBluetoothAdapter;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {//18以后
            mBluetoothAdapter = ((BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        } else {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return mBluetoothAdapter;
    }

    /**
     * 获取已经存在的adapter实例
     *
     * @return
     */
    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    /**
     * 判断手机手否支持蓝牙通信
     *
     * @return boolean
     */
    public boolean checkSupperBluetooth() {

        return mBluetoothAdapter == null;
    }

    /**
     * 检查蓝牙状态是否打开可用
     *
     * @return boolean
     */
    public boolean checkBluetoothEnable() {

        return mBluetoothAdapter.isEnabled();
    }


    /**
     * Api11+都可以使用的蓝牙扫描
     */
    public void doDiscovery() {
        mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * 控制蓝牙打开或者关闭
     *
     * @param state
     */
    public void openOrCloseBlue(boolean state) {
        if (mBluetoothAdapter == null) {
            return;
        }
        if (state) {
            mBluetoothAdapter.enable();
        } else {
            mBluetoothAdapter.disable();
        }
    }

    /**
     * 获得已知设备列表（配对）
     *
     * @return
     */
    public Set<BluetoothDevice> getBondedDevices() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.getBondedDevices();
        }
        return null;
    }
}
