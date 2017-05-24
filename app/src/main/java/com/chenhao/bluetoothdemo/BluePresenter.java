package com.chenhao.bluetoothdemo;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.chenhao.bluetoothdemo.adapter.BlueListAdapter;
import com.chenhao.bluetoothdemo.base.BaseMVPPresenter;
import com.chenhao.bluetoothlib.BluetoothUtils;
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
        getView().controlSearchText(-1);//默认是
    }

    @Override
    public void openOrCloseBlue(boolean isOpen) {
        if (isOpen) {
            bluetoothClient.openBluetooth(null);
        } else {
            bluetoothClient.closeBluetooth(null);
        }
    }

    @Override
    public void searchBluetoothList() {
        Log.d("BluePresenter", "执行");
        bluetoothClient.searchBluetoothDevices(null);
    }

    @Override
    public void connectBluetooth(String adress) {
        bluetoothClient.connectDevice(adress, null);
    }

    @Override
    public void discoverStart() {
        if (isViewAttached()) {
            getView().controlSearchText(0);
        }
    }

    @Override
    public void discoverEnd(ArrayList<BluetoothDevice> mBlueDevices) {
        if (isViewAttached()) {
            getView().controlSearchText(1);
        }
    }

    @Override
    public void bluetoothFound(BluetoothDevice bluetoothDevice) {
        if (isViewAttached()) {
            getView().loadBtListSuccess(bluetoothDevice);
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
                    Log.d("BluePresenter", "" + device.getName());
                    if (device.getAddress().equals(bluetoothDevice.getAddress())) {
                        Log.d("BluePresenter", "连接了" + device.getAddress() + "," + device.getName());
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
            getView().showToast("断开连接" + bluetoothDevice.getName());
            List<BluetoothDevice> currentBtList = getView().getCurrentBtList();
            if (currentBtList != null && currentBtList.size() > 0) {
                for (BluetoothDevice device : currentBtList) {
                    Log.d("BluePresenter", device.getName()+"");
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
