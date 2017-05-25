package com.chenhao.bluetoothdemo;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import com.chenhao.bluetoothlib.BluetoothUtils;
import com.chenhao.bluetoothlib.view.base.BaseMVPPresenter;
import com.chenhao.bluetoothlib.btinterface.IClientListenerContract;


/**
 * Created by chenhao on 2017/5/25.
 */

public class MessagePresenter extends BaseMVPPresenter<MessageContract.IView> implements MessageContract.IPresenter {

    private final BluetoothUtils instance;

    public MessagePresenter() {
        instance = BluetoothUtils.getInstance();
    }

    @Override
    public void sendMsg() {
        if (isViewAttached()) {
            String inputMsg = getView().getInputMsg();
            if (!TextUtils.isEmpty(inputMsg)) {
                instance.sendMessage(inputMsg.getBytes(), new IClientListenerContract.IDataSendListener() {
                    @Override
                    public void onDataSendSuccess(byte[] data) {
                        if (isViewAttached()) {
                            getView().showSendMsg(new String(data));
                        }
                    }

                    @Override
                    public void onDataSendFailure(String msg) {

                    }
                });
            }
        }

    }

    @Override
    public void asClientOrServer(boolean isClient, String address) {
        if (isClient && !TextUtils.isEmpty(address)) {
            instance.connectDevice(address, new IClientListenerContract.IConnectListener() {
                @Override
                public void onConnectSuccess(BluetoothDevice bluetoothDevice) {
                    if (isViewAttached()) {
                        getView().showTitleMsg(bluetoothDevice.getName() + "连接成功");
                    }
                }

                @Override
                public void onConnectFailure(String msg) {
                    if (isViewAttached()) {
                        getView().showTitleMsg("连接失败");
                    }
                }
            });
        }
        if (!isClient) {
            instance.openServer(new IClientListenerContract.IServerStatusListener() {
                @Override
                public void onGetClientSuccess(BluetoothDevice remoteDevice) {
                    if (isViewAttached()) {
                        getView().showTitleMsg(remoteDevice.getName() + "连接成功");
                    }
                }

                @Override
                public void onGetClientFailure(String message) {
                    if (isViewAttached()) {
                        getView().showTitleMsg("连接失败");
                    }
                }
            });
        }
    }

    @Override
    public void openReceiveService() {
        instance.recevieMessage(new IClientListenerContract.IDataReceiveListener() {
            @Override
            public void onDataSuccess(byte[] data, int length) {
                if (isViewAttached()) {
                    getView().showReceiveMsg(new String(data));
                }
            }

            @Override
            public void onDataFailure(String msg) {
                if (isViewAttached()) {
                    getView().showError("接收失败");
                }
            }
        });
    }
}
