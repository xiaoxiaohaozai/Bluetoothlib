package com.chenhao.bluetoothlib.btinterface;

/**
 * Created by chenhao on 2017/5/19.
 */

public interface OnConnectedListener {
    void onReadMessage(byte[] data, int length);

    void onWriteMessage(byte[] data);

    void onError(String error,boolean isRead);
}
