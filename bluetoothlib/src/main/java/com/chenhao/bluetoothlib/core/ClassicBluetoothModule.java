package com.chenhao.bluetoothlib.core;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import com.chenhao.bluetoothlib.btinterface.IClientListenerContract;
import com.chenhao.bluetoothlib.btinterface.BluetoothStatusListener;
import com.chenhao.bluetoothlib.btinterface.ICommonBTModule;
import com.chenhao.bluetoothlib.btinterface.OnAcceptListener;
import com.chenhao.bluetoothlib.btinterface.OnDataHandleListener;
import com.chenhao.bluetoothlib.constants.Constants;
import com.chenhao.bluetoothlib.receiver.BluetoothReceiver;
import com.chenhao.bluetoothlib.thread.AcceptThread;
import com.chenhao.bluetoothlib.thread.DataHandleThread;
import com.chenhao.bluetoothlib.utils.ByteUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chenhao on 2017/5/19.
 * 经典蓝牙模块
 */

public class ClassicBluetoothModule implements ICommonBTModule, BluetoothStatusListener {
    private static final String TAG = "ClassicBluetoothModule";
    private static ClassicBluetoothModule classicBluetoothModule;
    private BluetoothReceiver bluetoothReceiver;
    private Context mContext;
    private ArrayList<BluetoothDevice> mBondedList;
    private ArrayList<BluetoothDevice> mNewList;
    private ArrayList<BluetoothDevice> mBlueDevices;//所有得到设备
    private ExecutorService mExecutorService = Executors.newCachedThreadPool();//用于连接
    private BluetoothSocket mSocket;
    private DataHandleThread dataHandleThread;
    /**
     * 单个蓝牙状态的监听
     */
    private IClientListenerContract.ISearchDeviceListener onSearchDeviceListener;
    private IClientListenerContract.IDataReceiveListener mIDataReceiveListener;
    private IClientListenerContract.IConnectListener mConnectListener;
    private IClientListenerContract.IBlueClientIsOpenListener mIBlueClientIsOpenListener;
    private IClientListenerContract.IDataSendListener dataSendListener;
    //全局的蓝牙监听
    private List<IClientListenerContract.IBluetoothStatusListener> iBluetoothStatusListeners;
    //本地蓝牙
    private BluetoothAdapter bluetoothAdapter;
    private AcceptThread acceptThread;

    public static ClassicBluetoothModule newInstance(Context context) {
        if (classicBluetoothModule == null) {
            synchronized (ClassicBluetoothModule.class) {
                if (classicBluetoothModule == null) {
                    classicBluetoothModule = new ClassicBluetoothModule(context);
                }
            }
        }
        return classicBluetoothModule;
    }

    private ClassicBluetoothModule(Context context) {
        //TODO 注册蓝牙
        mContext = context;
        registerBluetooth();
        //获得蓝牙适配器
        bluetoothAdapter = getAdapter();
    }

    @Override
    public void searchBtDevices(IClientListenerContract.ISearchDeviceListener onSearchDeviceListener) {
        this.onSearchDeviceListener = onSearchDeviceListener;
        if (mBondedList == null) {
            mBondedList = new ArrayList<>();
        }
        if (mNewList == null) {
            mNewList = new ArrayList<>();
        }
        if (mBlueDevices == null) {
            mBlueDevices = new ArrayList<>();
        }
        mBondedList.clear();
        mNewList.clear();
        mBlueDevices.clear();
        if (!checkSupperBluetooth()) {
            if (checkBluetoothEnable()) {
                doDiscovery();
            }
        }
    }

    /**
     * 连接蓝牙
     *
     * @param address
     * @param iConnectListener
     */
    @Override
    public void connectBt(String address, IClientListenerContract.IConnectListener iConnectListener) {
        mConnectListener = iConnectListener;
        if (checkBluetoothEnable()) {
            ConnectDeviceRunnable connectDeviceRunnable = new ConnectDeviceRunnable(address);
            checkNotNull(mExecutorService);
            mExecutorService.submit(connectDeviceRunnable);
        }
    }


    /**
     * 连接蓝牙Runnable
     */
    private class ConnectDeviceRunnable implements Runnable {
        private String address;


        public ConnectDeviceRunnable(String address) {
            this.address = address;
        }

        @Override
        public void run() {
            bluetoothAdapter.cancelDiscovery();//取消搜索
            BluetoothDevice remoteDevice = bluetoothAdapter.getRemoteDevice(address);
            try {
                if (mSocket != null) {
                    mSocket.close();
                    mSocket = null;
                }
                mSocket = remoteDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(Constants.NEED_UUID));
            } catch (Exception e) {

            }
            if (dataHandleThread != null) {
                dataHandleThread.cancel();
                dataHandleThread = null;
            }
            try {
                mSocket.connect();
                dataHandleThread = new DataHandleThread(mSocket, dataHandleListener);
                dataHandleThread.start();
            } catch (IOException e) {
                e.printStackTrace();
                if (mConnectListener != null) {
                    mConnectListener.onConnectFailure(e.getMessage());
                }
                try {
                    mSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    /**
     * 数据处理线程监听
     */
    private OnDataHandleListener dataHandleListener = new OnDataHandleListener() {
        @Override
        public void onReadMessage(byte[] data, int length) {
            Log.d(TAG, "读取" + ByteUtils.toString(data));
            if (mIDataReceiveListener != null) {
                mIDataReceiveListener.onDataSuccess(data, length);
            }
        }

        @Override
        public void onWriteMessage(byte[] data) {
            Log.d(TAG, "写的" + ByteUtils.toString(data));
            if (dataSendListener != null) {
                dataSendListener.onDataSendSuccess(data);
            }

        }

        @Override
        public void onError(String error, boolean isRead) {
            Log.d(TAG, "错误" + error);
            if (mIDataReceiveListener != null && isRead) {
                mIDataReceiveListener.onDataFailure(error);
            }
            if (dataSendListener != null && !isRead) {
                dataSendListener.onDataSendFailure(error);
            }

        }
    };

    private void checkNotNull(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public void openBt(IClientListenerContract.IBlueClientIsOpenListener iBlueClientIsOpenListener) {
        this.mIBlueClientIsOpenListener = iBlueClientIsOpenListener;
        if (!checkSupperBluetooth()) {
            openOrCloseBlue(true);
        }
    }

    /**
     * 注册蓝牙
     */
    private void registerBluetooth() {
        bluetoothReceiver = new BluetoothReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//连接状态
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//每找到一个蓝牙
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//配对
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.setPriority(1000);
        mContext.registerReceiver(bluetoothReceiver, intentFilter);
    }

    /**
     * 解除注册
     */
    private void unregisterBluetooth() {
        if (bluetoothReceiver != null) {
            mContext.unregisterReceiver(bluetoothReceiver);
            bluetoothReceiver = null;
        }
    }

    @Override
    public void closeBt(IClientListenerContract.IBlueClientIsOpenListener iBlueClientIsOpenListener) {
        this.mIBlueClientIsOpenListener = iBlueClientIsOpenListener;
        //如果关闭时在扫描，取消扫描
        if (!checkSupperBluetooth()) {
            bluetoothAdapter.cancelDiscovery();
        }
        if (!checkSupperBluetooth()) {
            openOrCloseBlue(false);
        }
        if (dataHandleThread != null) {
            dataHandleThread.cancel();
            dataHandleThread = null;
        }
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                mSocket = null;
            }
        }
    }

    @Override
    public void sendMsg(byte[] data, IClientListenerContract.IDataSendListener dataSendListener) {
        this.dataSendListener = dataSendListener;
        if (dataHandleThread != null) {
            dataHandleThread.write(data);
        }

    }


    @Override
    public void readMes(IClientListenerContract.IDataReceiveListener iDataReceiveListener) {
        mIDataReceiveListener = iDataReceiveListener;
    }


    /**
     * 添加蓝牙状态监听
     *
     * @param iBluetoothStatusListener
     */
    @Override
    public void addBlueStatusListener(IClientListenerContract.IBluetoothStatusListener iBluetoothStatusListener) {
        if (iBluetoothStatusListeners == null) {
            iBluetoothStatusListeners = new CopyOnWriteArrayList<>();
        }
        if (!iBluetoothStatusListeners.contains(iBluetoothStatusListener)) {
            iBluetoothStatusListeners.add(iBluetoothStatusListener);
        }
    }

    /**
     * 移除蓝牙状态监听
     *
     * @param iBluetoothStatusListener
     */
    @Override
    public void removeBlueStatusListener(IClientListenerContract.IBluetoothStatusListener iBluetoothStatusListener) {
        if (iBluetoothStatusListeners != null && iBluetoothStatusListeners.contains(iBluetoothStatusListener)) {
            iBluetoothStatusListeners.remove(iBluetoothStatusListener);
        }
    }

    /**
     * 销毁
     */
    @Override
    public void onDestory() {
        unregisterBluetooth();
        reset();
        if (iBluetoothStatusListeners != null && iBluetoothStatusListeners.size() > 0) {
            iBluetoothStatusListeners.clear();
            iBluetoothStatusListeners = null;
        }
        if (dataHandleThread != null) {
            dataHandleThread.cancel();
        }
        dataHandleThread = null;
        mConnectListener = null;
        mIDataReceiveListener = null;
        mIBlueClientIsOpenListener = null;
        onSearchDeviceListener = null;
        dataSendListener = null;
    }

    @Override
    public boolean currentBtEnable() {
        if (!checkSupperBluetooth() && checkBluetoothEnable()) {
            return true;
        }
        return false;
    }

    @Override
    public void cancelBtSearch() {
        if (!checkSupperBluetooth() && checkBluetoothEnable()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    @Override
    public String getLocalName() {

        if (!checkSupperBluetooth()) {
            return bluetoothAdapter.getName();
        }
        return null;
    }

    /**
     * 开启服务器
     */
    @Override
    public void openServer(final IClientListenerContract.IServerStatusListener iServerStatusListener) {
        if (!checkSupperBluetooth() && checkBluetoothEnable()) {
            if (acceptThread != null) {
                acceptThread.cancel();
                acceptThread = null;
            }
            acceptThread = new AcceptThread(bluetoothAdapter, new OnAcceptListener() {
                @Override
                public void onGetClientSuccess(BluetoothSocket socket) {
                    if (iServerStatusListener != null) {
                        iServerStatusListener.onGetClientSuccess(socket.getRemoteDevice());
                    }
                    if (dataHandleThread != null) {
                        dataHandleThread.cancel();
                        dataHandleThread = null;
                    }
                    dataHandleThread = new DataHandleThread(socket, dataHandleListener);
                    dataHandleThread.start();
                }

                @Override
                public void onGetClientFailure(String message) {
                    if (iServerStatusListener != null) {
                        iServerStatusListener.onGetClientFailure(message);
                    }
                }
            });
            acceptThread.start();
        }

    }

    /**
     * 重置
     */
    public void reset() {
        if (bluetoothReceiver != null) {
            bluetoothReceiver.removeBluetoothStatusListener();
        }
        mNewList = null;
        mBondedList = null;
        mBlueDevices = null;
        bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter = null;
    }

    /********************蓝牙状态监听*******************/
    @Override
    public void discoverStarted() {
        Log.d("ClassicBluetoothModule", "开始扫描");
        if (onSearchDeviceListener != null) {
            onSearchDeviceListener.onSearchStart();
        }
        if (iBluetoothStatusListeners != null && iBluetoothStatusListeners.size() > 0) {
            for (IClientListenerContract.IBluetoothStatusListener listener : iBluetoothStatusListeners) {
                listener.discoverStart();
            }
        }
    }

    @Override
    public void discoverFinshed() {
        Log.d("ClassicBluetoothModule", "扫描结束");
        Log.d("ClassicBluetoothModule", "mBondedList:" + mBondedList.size() + "," + mBondedList);
        Log.d("ClassicBluetoothModule", "mNewList:" + mNewList.size() + "," + mNewList);
//        mBlueDevices.addAll(mNewList);
//        mBlueDevices.addAll(0, mBondedList);
        if (onSearchDeviceListener != null) {
            onSearchDeviceListener.onSearchEnd(mBlueDevices);
        }
        if (iBluetoothStatusListeners != null && iBluetoothStatusListeners.size() > 0) {
            for (IClientListenerContract.IBluetoothStatusListener listener : iBluetoothStatusListeners) {
                listener.discoverEnd(mBlueDevices);
            }
        }
    }

    @Override
    public void bluetoothFound(BluetoothDevice bluetoothDevice) {
        Log.d("ClassicBluetoothModule", "发现蓝牙" + bluetoothDevice);
        if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE) {
            if (mNewList.indexOf(bluetoothDevice) == -1) {//防止重复添加
                mNewList.add(bluetoothDevice);
            }

        } else if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
            mBondedList.add(bluetoothDevice);
        }
        if (mBlueDevices.indexOf(bluetoothDevice) == -1) {
            mBlueDevices.add(bluetoothDevice);
            if (iBluetoothStatusListeners != null && iBluetoothStatusListeners.size() > 0) {
                for (IClientListenerContract.IBluetoothStatusListener listener : iBluetoothStatusListeners) {
                    listener.bluetoothFound(bluetoothDevice);
                }
            }
        }
        if (onSearchDeviceListener != null) {
            onSearchDeviceListener.onFindDevice(bluetoothDevice);
        }

    }

    @Override
    public void bluetoothOpenSuccess() {
        Log.d("ClassicBluetoothModule", "蓝牙打开成功");
        if (mIBlueClientIsOpenListener != null) {
            mIBlueClientIsOpenListener.onOpen();
        }
        if (iBluetoothStatusListeners != null && iBluetoothStatusListeners.size() > 0) {
            for (IClientListenerContract.IBluetoothStatusListener listener : iBluetoothStatusListeners) {
                listener.bluetoothOpen();
            }
        }
    }

    @Override
    public void bluetoothCloseSuccess() {
        if (mIBlueClientIsOpenListener != null) {
            mIBlueClientIsOpenListener.onClose();
        }
        if (iBluetoothStatusListeners != null && iBluetoothStatusListeners.size() > 0) {
            for (IClientListenerContract.IBluetoothStatusListener listener : iBluetoothStatusListeners) {
                listener.bluetoothClose();
            }
        }
        Log.d("ClassicBluetoothModule", "蓝牙关闭成功");

    }

    @Override
    public void bluetoothConnected(BluetoothDevice bluetoothDevice) {
        Log.d("ClassicBluetoothModule", "蓝牙连接成功" + bluetoothDevice.getName());
        if (mConnectListener != null) {
            mConnectListener.onConnectSuccess(bluetoothDevice);
        }
        if (iBluetoothStatusListeners != null && iBluetoothStatusListeners.size() > 0) {
            for (IClientListenerContract.IBluetoothStatusListener listener : iBluetoothStatusListeners) {
                listener.bluetoothConnected(bluetoothDevice);
            }
        }
    }

    @Override
    public void bluetoothDisconnect(BluetoothDevice bluetoothDevice) {
        Log.d("ClassicBluetoothModule", "蓝牙连接失败" + bluetoothDevice.getName());
        if (dataHandleThread != null) {
            dataHandleThread.cancel();
        }
        if (mConnectListener != null) {
            mConnectListener.onConnectFailure("");
        }
        if (iBluetoothStatusListeners != null && iBluetoothStatusListeners.size() > 0) {
            for (IClientListenerContract.IBluetoothStatusListener listener : iBluetoothStatusListeners) {
                listener.bluetoothDisconnect(bluetoothDevice);
            }
        }
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
     * 判断手机手否支持蓝牙通信
     *
     * @return boolean
     */
    private boolean checkSupperBluetooth() {

        return bluetoothAdapter == null;
    }

    /**
     * 检查蓝牙状态是否打开可用
     *
     * @return boolean
     */
    private boolean checkBluetoothEnable() {

        return bluetoothAdapter.isEnabled();
    }


    /**
     * Api11+都可以使用的蓝牙扫描
     */
    private void doDiscovery() {
        bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.startDiscovery();
    }

    /**
     * 控制蓝牙打开或者关闭
     *
     * @param state
     */
    private void openOrCloseBlue(boolean state) {
        if (bluetoothAdapter == null) {
            return;
        }
        if (state) {
            bluetoothAdapter.enable();
        } else {
            bluetoothAdapter.disable();
        }
    }

    /**
     * 获得已知设备列表（配对）
     *
     * @return
     */
    private Set<BluetoothDevice> getBondedDevices() {
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.getBondedDevices();
        }
        return null;
    }
}
