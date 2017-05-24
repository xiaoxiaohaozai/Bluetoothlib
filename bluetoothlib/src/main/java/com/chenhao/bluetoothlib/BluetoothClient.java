package com.chenhao.bluetoothlib;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.chenhao.bluetoothlib.btinterface.IBtClient;
import com.chenhao.bluetoothlib.btinterface.ICommonBTModule;
import com.chenhao.bluetoothlib.core.ClassicBluetoothModule;
import com.chenhao.bluetoothlib.utils.BluetoothHelper;

/**
 * Created by chenhao on 2017/5/19.
 */

public class BluetoothClient implements IBtClient {

    private static BluetoothClient bluetoothClient;
    private Context applicationContext;
    //默认是标准蓝牙
    private ICommonBTModule iCommonBTModule;

    private BluetoothClient(Context context) {
        applicationContext = context.getApplicationContext();
        iCommonBTModule = ClassicBluetoothModule.newInstance(applicationContext);//添加蓝牙模块
    }

    /**
     * application中初始化
     *
     * @param context
     */
    public static void init(Context context) {
        if (bluetoothClient == null) {
            synchronized (BluetoothClient.class) {
                if (bluetoothClient == null) {
                    bluetoothClient = new BluetoothClient(context);
                }
            }
        }
    }

    /**
     * 获得蓝牙客户端实例
     *
     * @return
     */
    public static BluetoothClient getInstance() {
        if (bluetoothClient != null) {
            return bluetoothClient;
        }
        return null;
    }

    /**
     * 检查蓝牙模块是否为null
     *
     * @return
     */
    private boolean checkBtModuleNull() {
        if (iCommonBTModule != null) {
            return true;
        }
        return false;
    }

    /**
     * 绑定蓝牙模块
     *
     * @param iCommonBTModule
     */
    @Override
    public void bindBlueModule(ICommonBTModule iCommonBTModule) {
        this.iCommonBTModule = iCommonBTModule;
    }

    /**
     * 寻找蓝牙
     *
     * @param onSearchDeviceListener
     */
    @Override
    public void searchBluetoothDevices(IClientListenerContract.ISearchDeviceListener onSearchDeviceListener) {
        if (checkBtModuleNull()) {
            Log.d("BluetoothClient", "执行");
            iCommonBTModule.searchBtDevices(onSearchDeviceListener);
        }

    }

    /**
     * 连接设备
     *
     * @param address
     * @param iConnectListener
     */
    @Override
    public void connectDevice(String address, IClientListenerContract.IConnectListener iConnectListener) {
        if (checkBtModuleNull() && !TextUtils.isEmpty(address)) {
            iCommonBTModule.connectBt(address, iConnectListener);
        }

    }

    /**
     * 打开蓝牙
     *
     * @param iBlueClientIsOpenListener
     */
    @Override
    public void openBluetooth(IClientListenerContract.IBlueClientIsOpenListener iBlueClientIsOpenListener) {
        if (checkBtModuleNull()) {
            iCommonBTModule.openBt(iBlueClientIsOpenListener);
        }

    }

    /**
     * 关闭蓝牙
     *
     * @param iBlueClientIsOpenListener
     */
    @Override
    public void closeBluetooth(IClientListenerContract.IBlueClientIsOpenListener iBlueClientIsOpenListener) {
        if (checkBtModuleNull()) {
            iCommonBTModule.closeBt(iBlueClientIsOpenListener);
        }
    }

    /**
     * 发送信息
     *
     * @param address
     * @param data
     */
    @Override
    public void sendMessage(String address, byte[] data) {
        if (checkBtModuleNull() && !TextUtils.isEmpty(address) && data != null) {
            iCommonBTModule.sendMsg(address, data);
        }
    }

    /**
     * 接收信息
     *
     * @param iDataReceiveListener
     */
    @Override
    public void recevieMessage(IClientListenerContract.IDataReceiveListener iDataReceiveListener) {
        if (checkBtModuleNull()) {
            iCommonBTModule.readMes(iDataReceiveListener);
        }
    }

    /**
     * 销毁蓝牙客户端
     */
    @Override
    public void onDestroy() {
        if (checkBtModuleNull()) {
            iCommonBTModule.onDestory();
            iCommonBTModule = null;
        }

    }

    /**
     * 对蓝牙状态的监听
     *
     * @param iBluetoothStatusListener
     */
    public void addBlueStasusLitener(IClientListenerContract.IBluetoothStatusListener iBluetoothStatusListener) {
        if (checkBtModuleNull()) {
            iCommonBTModule.addBlueStatusListener(iBluetoothStatusListener);
        }
    }

    /**
     * 清除对蓝牙状态的监听
     *
     * @param iBluetoothStatusListener
     */
    public void removeBlueStatusLisener(IClientListenerContract.IBluetoothStatusListener iBluetoothStatusListener) {
        if (checkBtModuleNull()) {
            iCommonBTModule.removeBlueStatusListener(iBluetoothStatusListener);
        }
    }

    public boolean getCurrentBluetoothEnable() {
        return iCommonBTModule.currentBtEnable();
    }


}
