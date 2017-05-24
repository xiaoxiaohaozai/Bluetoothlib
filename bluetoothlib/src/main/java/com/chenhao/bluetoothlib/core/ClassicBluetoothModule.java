package com.chenhao.bluetoothlib.core;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import com.chenhao.bluetoothlib.IClientListenerContract;
import com.chenhao.bluetoothlib.btinterface.BluetoothStatusListener;
import com.chenhao.bluetoothlib.btinterface.ICommonBTModule;
import com.chenhao.bluetoothlib.btinterface.OnConnectedListener;
import com.chenhao.bluetoothlib.constants.Constants;
import com.chenhao.bluetoothlib.receiver.BluetoothReceiver;
import com.chenhao.bluetoothlib.thread.ConnectedThread;
import com.chenhao.bluetoothlib.utils.BluetoothHelper;
import com.chenhao.bluetoothlib.utils.ByteUtils;
import com.chenhao.bluetoothlib.utils.ClsUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private final BluetoothHelper bluetoothHelper;
    private BluetoothReceiver bluetoothReceiver;
    private Context mContext;

    private ArrayList<BluetoothDevice> mBondedList;
    private ArrayList<BluetoothDevice> mNewList;
    private ArrayList<BluetoothDevice> mBlueDevices;//所有得到设备
    private ExecutorService mExecutorService = Executors.newCachedThreadPool();

    public volatile STATUS mCurrStatus = STATUS.DISCONNECTED;//默认未连接状态
    private BluetoothSocket mSocket;
    private ConnectedThread connectedThread;
    /**
     * 蓝牙状态的监听
     */
    private IClientListenerContract.ISearchDeviceListener onSearchDeviceListener;
    private IClientListenerContract.IDataReceiveListener mIDataReceiveListener;
    private IClientListenerContract.IConnectListener mConnectListener;
    private IClientListenerContract.IBlueClientIsOpenListener mIBlueClientIsOpenListener;
    private List<IClientListenerContract.IBluetoothStatusListener> iBluetoothStatusListeners;


    /**
     * 蓝牙状态
     */
    private enum STATUS {
        CONNECTED,
        CONNETING,
        DISCONNECTED
    }


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
        mContext = context;
        bluetoothHelper = BluetoothHelper.getInstance(context.getApplicationContext());
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
        if (!bluetoothHelper.checkSupperBluetooth()) {
            if (bluetoothHelper.checkBluetoothEnable()) {
                bluetoothHelper.doDiscovery();
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
        if (BluetoothHelper.getInstance(mContext).checkBluetoothEnable()) {
            ConnectDeviceRunnable connectDeviceRunnable = new ConnectDeviceRunnable(address);
            checkNotNull(mExecutorService);
            mExecutorService.submit(connectDeviceRunnable);
        }
    }

    private int connectTimes = 0;

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
            bluetoothHelper.getBluetoothAdapter().cancelDiscovery();//取消搜索
            BluetoothDevice remoteDevice = bluetoothHelper.getBluetoothAdapter().getRemoteDevice(address);
            mCurrStatus = STATUS.CONNETING;
            try {
                if (mSocket != null) {
                    mSocket.close();
                    mSocket = null;
                }
                Log.d(TAG, "准备连接: " + bluetoothHelper.getBluetoothAdapter().getAddress() + " " + bluetoothHelper.getBluetoothAdapter().getName());
                mSocket = remoteDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(Constants.NEED_UUID));
            } catch (Exception e) {

            }
            connectTimes = 0;
            while (mCurrStatus == STATUS.CONNETING && connectTimes <= 5) {
                Log.d(TAG, "连接" + connectTimes);
                connectDevice(remoteDevice);
            }
            if (mCurrStatus == STATUS.CONNETING) {
                mCurrStatus = STATUS.DISCONNECTED;
            }
        }
    }

    private void connectDevice(BluetoothDevice remoteDevice) {
//        //先自动配对
//        if (remoteDevice.getBondState() == BluetoothDevice.BOND_NONE) {
//            try {
//                ClsUtils.createBond(remoteDevice.getClass(), remoteDevice);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.d(TAG, "e:" + e);
//            }
//        }
        try {
            //创建数据处理线程
            if (connectedThread != null) {
                connectedThread.cancel();
                connectedThread = null;
            }
            mSocket.connect();
            connectedThread = new ConnectedThread(mSocket, OnConnected);
            connectedThread.start();
            mCurrStatus = STATUS.CONNECTED;
            connectTimes = 0;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "e:" + e);
            connectTimes++;
            mCurrStatus = STATUS.CONNETING;
            if (mConnectListener != null) {
                mConnectListener.onConnectFailure(e.getMessage());
            }
        }


    }


    /**
     * 线程监听
     */
    private OnConnectedListener OnConnected = new OnConnectedListener() {
        @Override
        public void onReadMessage(byte[] data) {
            Log.d(TAG, "读取" + ByteUtils.toString(data));
            if (mIDataReceiveListener != null) {
                mIDataReceiveListener.onDataSuccess(data);
            }
        }

        @Override
        public void onWriteMessage(byte[] data) {
            Log.d(TAG, "写的" + ByteUtils.toString(data));

        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "错误" + error);
            if (mIDataReceiveListener != null) {
                mIDataReceiveListener.onDataFailure(error);
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
        //TODO 注册蓝牙
        registerBluetooth();//监听里是蓝牙打开才添加t
        if (!bluetoothHelper.checkSupperBluetooth()) {
            bluetoothHelper.openOrCloseBlue(true);
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
        //如果关闭时在扫描，取消扫描
        if (!bluetoothHelper.checkSupperBluetooth()) {
            bluetoothHelper.getBluetoothAdapter().cancelDiscovery();
        }
        if (!bluetoothHelper.checkSupperBluetooth()) {
            bluetoothHelper.openOrCloseBlue(false);
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
    }

    @Override
    public void sendMsg(String address, byte[] data) {
        //  TODO 重新连接
        if (mCurrStatus != STATUS.CONNECTED) {
            //connectBt(address);

        }
        if (connectedThread != null) {
            connectedThread.write(data);
        }

    }


    @Override
    public void readMes(IClientListenerContract.IDataReceiveListener iDataReceiveListener) {
        mIDataReceiveListener = iDataReceiveListener;
    }

    @Override
    public void disconnectBt() {
        //关闭信道
        if (mSocket != null)
            try {
                mSocket.close();
            } catch (IOException e) {
                mSocket = null;
            }

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
        disconnectBt();
        reset();
        mConnectListener = null;
        mIDataReceiveListener = null;
        mIBlueClientIsOpenListener = null;
        onSearchDeviceListener = null;
        bluetoothReceiver = null;
        connectedThread = null;
    }

    @Override
    public boolean currentBtEnable() {
        if (!BluetoothHelper.getInstance(mContext).checkSupperBluetooth() && BluetoothHelper.getInstance(mContext).checkBluetoothEnable()) {
            return true;
        }
        return false;
    }


    /**
     * 重置
     */
    public void reset() {
        if (bluetoothReceiver != null) {
            bluetoothReceiver.removeBluetoothStatusListener();
        }
        unregisterBluetooth();
        mNewList = null;
        mBondedList = null;
        mBlueDevices = null;
        mCurrStatus = STATUS.DISCONNECTED;
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
        mBlueDevices.addAll(mNewList);
        mBlueDevices.addAll(0, mBondedList);
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
        if (onSearchDeviceListener != null) {
            onSearchDeviceListener.onFindDevice(bluetoothDevice);
        }
        if (iBluetoothStatusListeners != null && iBluetoothStatusListeners.size() > 0) {
            for (IClientListenerContract.IBluetoothStatusListener listener : iBluetoothStatusListeners) {
                listener.bluetoothFound(bluetoothDevice);
            }
        }
    }

    @Override
    public void bluetoothOpenSuccess() {
        mCurrStatus = STATUS.DISCONNECTED;
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
        mCurrStatus = STATUS.DISCONNECTED;
        if (mIBlueClientIsOpenListener != null) {
            mIBlueClientIsOpenListener.onClose();
        }
        if (iBluetoothStatusListeners != null && iBluetoothStatusListeners.size() > 0) {
            for (IClientListenerContract.IBluetoothStatusListener listener : iBluetoothStatusListeners) {
                listener.bluetoothClose();
            }
        }
        Log.d("ClassicBluetoothModule", "蓝牙关闭成功");
        reset();

    }

    @Override
    public void bluetoothConnected(BluetoothDevice bluetoothDevice) {
        mCurrStatus = STATUS.CONNECTED;
        Log.d("ClassicBluetoothModule", "蓝牙连接成功"+bluetoothDevice.getName());
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
        Log.d("ClassicBluetoothModule", "蓝牙连接失败"+bluetoothDevice.getName());
        mCurrStatus = STATUS.DISCONNECTED;
        if (connectedThread != null) {
            connectedThread.cancel();
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
}
