package com.test.bluedemo;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chenhao.bluetoothlib.BluetoothUtils;
import com.chenhao.bluetoothlib.btinterface.IClientListenerContract;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_open;
    private Button bt_close;
    private Button bt_serch;
    private Button bt_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        BluetoothUtils.init(this);
    }

    private void initViews() {
        bt_open = (Button) findViewById(R.id.bt_open);
        bt_close = (Button) findViewById(R.id.bt_close);
        bt_serch = (Button) findViewById(R.id.bt_serch);
        bt_connect = (Button) findViewById(R.id.bt_connect);
        bt_open.setOnClickListener(this);
        bt_close.setOnClickListener(this);
        bt_serch.setOnClickListener(this);
        bt_connect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_open:
                BluetoothUtils.getInstance().openBluetooth(null);
                break;
            case R.id.bt_close:
                BluetoothUtils.getInstance().closeBluetooth(null);
                break;
            case R.id.bt_serch:
                BluetoothUtils.getInstance().searchBluetoothDevices(new IClientListenerContract.ISearchDeviceListener() {
                    @Override
                    public void onSearchStart() {

                    }

                    @Override
                    public void onFindDevice(BluetoothDevice bluetoothDevice) {

                    }

                    @Override
                    public void onSearchEnd(List<BluetoothDevice> bluetoothDevices) {

                    }
                });
                break;
            case R.id.bt_connect:
                BluetoothUtils.getInstance().connectDevice("", new IClientListenerContract.IConnectListener() {
                    @Override
                    public void onConnectSuccess(BluetoothDevice bluetoothDevice) {

                    }

                    @Override
                    public void onConnectFailure(String msg) {

                    }

                    @Override
                    public void onConnecting() {

                    }
                });
                break;
        }
    }

    /**
     * 发送消息
     */
    public void setMessage(){
        BluetoothUtils.getInstance().sendMessage(new byte[]{}, new IClientListenerContract.IDataSendListener() {
            @Override
            public void onDataSendSuccess(byte[] data) {

            }

            @Override
            public void onDataSendFailure(String msg) {

            }
        });
    }

    /**
     * 获得消息
     */
    public void getMessage(){
        BluetoothUtils.getInstance().recevieMessage(new IClientListenerContract.IDataReceiveListener() {
            @Override
            public void onDataSuccess(byte[] data, int length) {

            }

            @Override
            public void onDataFailure(String msg) {

            }
        });
    }

    public void listenStatus(){
        BluetoothUtils.getInstance().addBlueStasusLitener(new IClientListenerContract.IBluetoothStatusListener() {
            @Override
            public void discoverStart() {

            }

            @Override
            public void discoverEnd(ArrayList<BluetoothDevice> mBlueDevices) {

            }

            @Override
            public void bluetoothFound(BluetoothDevice bluetoothDevice) {

            }

            @Override
            public void bluetoothOpen() {

            }

            @Override
            public void bluetoothClose() {

            }

            @Override
            public void bluetoothConnectFailure() {

            }

            @Override
            public void bluetoothConnected(BluetoothDevice bluetoothDevice) {

            }

            @Override
            public void bluetoothDisconnect() {

            }
        });
    }
}
