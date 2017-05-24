package com.chenhao.bluetoothlib.thread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.chenhao.bluetoothlib.btinterface.OnAcceptListener;
import com.chenhao.bluetoothlib.btinterface.OnDataHandleListener;
import com.chenhao.bluetoothlib.constants.Constants;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by chenhao on 2017/5/24.
 * 服务器开启开线程
 */

public class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private OnAcceptListener onAcceptListener;

    public AcceptThread(BluetoothAdapter bluetoothAdapter, OnAcceptListener onAcceptListener) {
        this.onAcceptListener = onAcceptListener;
        BluetoothServerSocket tmp = null;
        try {
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(Constants.NAME, UUID.fromString(Constants.NEED_UUID));
        } catch (IOException e) {
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        while (true) {
            try {
                socket = mmServerSocket.accept();
                if (onAcceptListener != null) {
                    onAcceptListener.onGetClientSuccess(socket);
                }
            } catch (IOException e) {
                if (onAcceptListener != null) {
                    onAcceptListener.onGetClientFailure(e.getMessage());
                }
                break;
            }
            if (socket != null) {
                try {
                    //这将释放服务器套接字及其所有资源，但不会关闭 accept() 所返回的已连接的 BluetoothSocket
                    mmServerSocket.close();//这是为了一次只连接一个客户端，也可以通时连接，做多是7个
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * Will cancel the listening socket, and cause the thread to finish
     */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
        }
    }

}
