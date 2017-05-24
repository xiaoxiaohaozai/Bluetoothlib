package com.chenhao.bluetoothdemo;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chenhao.bluetoothdemo.adapter.BlueListAdapter;
import com.chenhao.bluetoothlib.BluetoothClient;
import com.chenhao.bluetoothlib.IClientListenerContract;
import com.chenhao.bluetoothlib.utils.BluetoothHelper;
import com.chenhao.bluetoothlib.utils.ByteUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private View open;
    private View close;
    private View button2;
    private View bt_1;
    private View bt_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BluetoothClient.init(this);
        open = findViewById(R.id.open);
        close = findViewById(R.id.close);
        button2 = findViewById(R.id.button2);
        bt_1 = findViewById(R.id.bt_1);
        bt_2 = findViewById(R.id.bt_2);
        lv = (ListView) findViewById(R.id.lv);
        initListener();

    }

    private void initListener() {

        BluetoothClient.getInstance().recevieMessage(new IClientListenerContract.IDataReceiveListener() {
            @Override
            public void onDataSuccess(byte[] data) {
                Log.d("MainActivity", "data:" + ByteUtils.toString(data));
                //Toast.makeText(MainActivity.this, ByteUtils.toString(data), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDataFailure(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothClient.getInstance().sendMessage("00:0C:BF:16:37:E1", new byte[]{(byte) 0xF3, (byte) 0xf4, 0x07, (byte) 0x91, 0x21, 0x01, 0x33, 0x52, (byte) 0xb2, (byte) 0xf1, (byte) 0xfc});
            }
        });

    }


}
