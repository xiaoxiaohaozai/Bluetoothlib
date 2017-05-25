package com.chenhao.bluetoothlib.view;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.util.Log;

import com.chenhao.bluetoothlib.BluetoothUtils;
import com.chenhao.bluetoothlib.view.base.BaseMVPPresenter;
import com.chenhao.bluetoothlib.btinterface.IClientListenerContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhao on 2017/5/21.
 */

public class BluePresenter
        extends BaseMVPPresenter<BlueContract.IView>
        implements BlueContract.IPrensenter, IClientListenerContract.IBluetoothStatusListener {

    private final BluetoothUtils bluetoothClient;

    public BluePresenter() {
        bluetoothClient = BluetoothUtils.getInstance();
    }

    @Override
    public void init() {
        bluetoothClient.addBlueStasusLitener(this);
        if (!isViewAttached()) {
            return;
        }
        getView().controlBlueButton(bluetoothClient.getCurrentBluetoothEnable());
        String localDeviceName = bluetoothClient.getLocalDeviceName();
        if (!TextUtils.isEmpty(localDeviceName)) {
            getView().setLocalDeviceName("名称：" + localDeviceName);
        } else {
            getView().setLocalDeviceName("名称：无法读取");
        }
    }

    @Override
    public void openOrCloseBlue(boolean isOpen) {
        if (isOpen) {
            bluetoothClient.openBluetooth(null);
        } else {
            bluetoothClient.closeBluetooth(null);
            getView().clearBtList();
        }
    }

    @Override
    public void searchBluetoothList() {
        if (isViewAttached()){
            getView().clearBtList();
        }
        bluetoothClient.searchBluetoothDevices(null);
    }

    @Override
    public void connectBluetooth(String adress) {
        bluetoothClient.connectDevice(adress, null);
    }

    @Override
    public void userDefined(BluetoothDevice bluetoothDevice) {
        if (isViewAttached()) {
            getView().handleIntent(bluetoothDevice);
        }
    }

    @Override
    public void back() {
        bluetoothClient.cancelBluetoothSearch();
        if (isViewAttached()) {
            getView().handeleback();
        }
    }


    /*****************蓝牙状态监听*****************/
    @Override
    public void discoverStart() {
        if (isViewAttached()) {
            getView().showLoading();
        }
    }

    @Override
    public void discoverEnd(ArrayList<BluetoothDevice> mBlueDevices) {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }

    @Override
    public void bluetoothFound(BluetoothDevice bluetoothDevice) {
        if (isViewAttached()) {
            getView().foundSingleDevice(bluetoothDevice);
        }
    }

    @Override
    public void bluetoothOpen() {
        if (isViewAttached()) {
            getView().controlBlueButton(true);
        }
        bluetoothClient.searchBluetoothDevices(null);
    }

    @Override
    public void bluetoothClose() {
        if (isViewAttached()) {
            getView().hideLoading();
            getView().controlBlueButton(false);
        }

    }

    @Override
    public void bluetoothConnected(BluetoothDevice bluetoothDevice) {
        if (isViewAttached()) {
            getView().showToast("成功连接" + bluetoothDevice.getName());
            List<BluetoothDevice> currentBtList = getView().getCurrentBtList();
            if (currentBtList != null && currentBtList.size() > 0) {
                for (BluetoothDevice device : currentBtList) {
                    if (device.getAddress().equals(bluetoothDevice.getAddress())) {
                        BlueListAdapter blueListAdapter = getView().getBlueListAdapter();
                        blueListAdapter.setCurrentConnectBt(bluetoothDevice);
                        getView().updateBtList();
                        break;
                    }
                }
            }

        }

    }

    @Override
    public void bluetoothDisconnect(BluetoothDevice bluetoothDevice) {
        if (isViewAttached()) {
            List<BluetoothDevice> currentBtList = getView().getCurrentBtList();
            if (currentBtList != null && currentBtList.size() > 0) {
                for (BluetoothDevice device : currentBtList) {
                    if (device.getAddress().equals(bluetoothDevice.getAddress())) {
                        Log.d("BluePresenter", "断开连接" + device.getAddress() + "," + device.getName());
                        BlueListAdapter blueListAdapter = getView().getBlueListAdapter();
                        blueListAdapter.setCurrentDisConnectBt(bluetoothDevice);
                        getView().updateBtList();
                        break;
                    }
                }
            }
        }
    }
}
