package com.chenhao.bluetoothlib.thread;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.chenhao.bluetoothlib.btinterface.OnConnectedListener;
import com.chenhao.bluetoothlib.utils.ByteUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by chenhao on 2017/5/19.
 * 连接数据处理线程
 */

public class ConnectedThread extends Thread {
    private static final int READ_LENGTH = 64;
    private BluetoothSocket mBluetoothSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private OnConnectedListener mOnConnectedListener;
    public static final int READ_MESSAGE = 0x01;
    public static final int WRITE_MESSAGE = 0x02;
    public static final int ERROR_MESSAGE = 0x03;
    /**
     * 处理收到消息
     */
    Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            if (mOnConnectedListener == null) {
                return;
            }
            switch (msg.what) {
                case READ_MESSAGE:
                    mOnConnectedListener.onReadMessage((byte[]) msg.obj);
                    break;
                case WRITE_MESSAGE:
                    mOnConnectedListener.onWriteMessage((byte[]) msg.obj);
                    break;
                case ERROR_MESSAGE:
                    mOnConnectedListener.onError((String) msg.obj);
                    break;
            }
        }
    };


    public ConnectedThread(BluetoothSocket bluetoothSocket, OnConnectedListener onConnectedListener) {
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
                    int data = mInputStream.read(btButTmp);
                    byte[] receiver = Arrays.copyOf(btButTmp, data);
                    Log.d("ConnectedThread", "bluetooth receiver==" + ByteUtils.toString(receiver));
                    Message message = handler.obtainMessage();
                    message.what = READ_MESSAGE;
                    message.obj = receiver;
                    handler.sendMessage(message);
                }
            } catch (IOException e) {
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
        Message message = handler.obtainMessage();
        try {
            mOutputStream.write(bytes);
            message.what = WRITE_MESSAGE;
            message.obj = bytes;
        } catch (IOException e) {
            e.printStackTrace();
            message.what = ERROR_MESSAGE;
            message.obj = bytes;
        }
        handler.sendMessage(message);
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
