package com.chenhao.bluetoothdemo.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.chenhao.bluetoothdemo.R;
import com.chenhao.bluetoothlib.view.BlueFragment;


/**
 * Created by chenhao on 2017/5/25.
 */

public class BlueListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layout);
        BlueFragment blueFragment = BlueFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl,blueFragment).commit();
        blueFragment.setOnUserActionListener(new BlueFragment.OnUserActionListener() {
            @Override
            public void onBack() {
                Toast.makeText(BlueListActivity.this, "点击了返回", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onHandleIntent(BluetoothDevice bluetoothDevice) {
                MessageActivity.showMessageActivity(BlueListActivity.this,true,bluetoothDevice.getAddress());
            }
        });
    }
}
