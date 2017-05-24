package com.chenhao.bluetoothlib.btinterface;

/**
 * Created by chenhao on 2017/5/19.
 *
 */

public interface OnConnectedListener {
    void onReadMessage(byte[] data);
    void onWriteMessage(byte[] data);
    void onError(String error);
}
