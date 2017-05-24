package com.chenhao.bluetoothlib.thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.chenhao.bluetoothlib.btinterface.OnDataHandleListener;
import com.chenhao.bluetoothlib.utils.ByteUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by chenhao on 2017/5/19.
 * 连接数据处理线程
 */

public class DataHandleThread extends Thread {
    private static final int READ_LENGTH = 64;
    private BluetoothSocket mBluetoothSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private OnDataHandleListener mOnConnectedListener;


    public DataHandleThread(BluetoothSocket bluetoothSocket, OnDataHandleListener onConnectedListener) {
        mBluetoothSocket = bluetoothSocket;
        mOnConnectedListener = onConnectedListener;
        InputStream tempInputStream = null;
        OutputStream tempOutputStream = null;
        try {
            tempOutputStream = bluetoothSocket.getOutputStream();
            tempInputStream = bluetoothSocket.getInputStream();
        } catch (Exception e) {

        }
        mInputStream = tempInputStream;
        mOutputStream = tempOutputStream;
    }


    /**
     * 子线程用于读取数据
     */
    @Override
    public void run() {

        while (!isInterrupted()) {
            byte[] btButTmp = new byte[READ_LENGTH];
            try {
                if (mInputStream != null) {
                    int length = mInputStream.read(btButTmp);
                    byte[] receiver = Arrays.copyOf(btButTmp, length);
                    Log.d("ConnectedThread", "bluetooth receiver==" + ByteUtils.toString(receiver));
                    if (mOnConnectedListener != null) {
                        mOnConnectedListener.onReadMessage(receiver, length);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (mOnConnectedListener != null) {
                    mOnConnectedListener.onError(e.getMessage(), true);
                }
                break;
            }
        }
    }

    /**
     * 写数据
     *
     * @param bytes
     */
    public void write(byte[] bytes) {
        try {
            mOutputStream.write(bytes);
            if (mOnConnectedListener != null) {
                mOnConnectedListener.onWriteMessage(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (mOnConnectedListener != null) {
                mOnConnectedListener.onError(e.getMessage(), false);
            }
        }
    }

    /**
     * 取消
     */
    public void cancel() {
        try {
            mBluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
