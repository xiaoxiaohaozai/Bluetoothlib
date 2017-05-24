package com.chenhao.bluetoothlib;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.chenhao.bluetoothlib.btinterface.IBluetoothUtilsListener;
import com.chenhao.bluetoothlib.btinterface.IClientListenerContract;
import com.chenhao.bluetoothlib.btinterface.ICommonBTModule;
import com.chenhao.bluetoothlib.btinterface.OnAcceptListener;
import com.chenhao.bluetoothlib.core.ClassicBluetoothModule;
import com.chenhao.bluetoothlib.thread.AcceptThread;

/**
 * Created by chenhao on 2017/5/19.
 */

public class BluetoothUtils implements IBluetoothUtilsListener {

    private static BluetoothUtils bluetoothClient;
    private Context applicationContext;
    //默认是标准蓝牙
    private ICommonBTModule iCommonBTModule;

    private BluetoothUtils(Context context) {
        applicationContext = context.getApplicationContext();
        //添加蓝牙模块
        iCommonBTModule = ClassicBluetoothModule.newInstance(applicationContext);
    }

    /**
     * application中初始化
     *
     * @param context
     */
    public static void init(Context context) {
        if (bluetoothClient == null) {
            synchronized (BluetoothUtils.class) {
                if (bluetoothClient == null) {
                    bluetoothClient = new BluetoothUtils(context);
                }
            }
        }
    }

    /**
     * 获得蓝牙客户端实例
     *
     * @return
     */
    public static BluetoothUtils getInstance() {
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
     * @param data
     */
    @Override
    public void sendMessage(byte[] data, IClientListenerContract.IDataSendListener dataSendListener) {
        if (checkBtModuleNull() && data != null) {
            iCommonBTModule.sendMsg(data, dataSendListener);
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
     * 取消蓝牙搜索
     */
    @Override
    public void cancelBluetoothSearch() {
        if (checkBtModuleNull()) {
            iCommonBTModule.cancelBtSearch();
        }
    }

    /**
     * 得到本地蓝牙名称
     *
     * @return
     */
    @Override
    public String getLocalDeviceName() {
        if (checkBtModuleNull()) {
            return iCommonBTModule.getLocalName();
        }
        return null;
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

    /**
     * 销毁蓝牙客户端
     */
    @Override
    public void onDestroy() {
        if (checkBtModuleNull()) {
            iCommonBTModule.onDestory();
            iCommonBTModule = null;
        }
        applicationContext = null;
        bluetoothClient = null;
    }

    /**
     * 当前蓝牙的开关状态
     *
     * @return
     */
    @Override
    public boolean getBluetoothEnable() {
        return false;
    }

    /**
     * 打开服务器
     *
     * @param
     */
    public void openServer(IClientListenerContract.IServerStatusListener iServerStatusListener) {
        if (checkBtModuleNull()) {
            iCommonBTModule.openServer(iServerStatusListener);
        }
    }
}
