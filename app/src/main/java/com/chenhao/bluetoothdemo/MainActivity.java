package com.chenhao.bluetoothdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.chenhao.bluetoothlib.BluetoothClient;
import com.chenhao.bluetoothlib.IClientListenerContract;
import com.chenhao.bluetoothlib.utils.ByteUtils;

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
            public void onDataSuccess(byte[] data, int length) {
                Log.d("MainActivity", "data:" + ByteUtils.toString(data));
            }

            @Override
            public void onDataFailure(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }


}
