package com.chenhao.bluetoothdemo;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.chenhao.bluetoothdemo.adapter.BlueListAdapter;
import com.chenhao.bluetoothdemo.base.MVPBaseFragment;
import com.chenhao.bluetoothdemo.utils.ToastUtils;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhao on 2017/5/21.
 */

public class BlueFragment
        extends MVPBaseFragment<BlueContract.IView, BlueContract.IPrensenter>
        implements BlueContract.IView {

    private TextView tv_bt_search_msg;
    private SwipeRefreshLayout srfl;
    private SwitchButton sb_bt_control;
    private ListView lv_blue_list;
    private BlueListAdapter blueListAdapter;
    private List<BluetoothDevice> bluetoothDevices;
    private TextView tv_title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth_layout, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        tv_bt_search_msg = (TextView) view.findViewById(R.id.tv_bt_search_msg);
        srfl = (SwipeRefreshLayout) view.findViewById(R.id.srfl);
        sb_bt_control = (SwitchButton) view.findViewById(R.id.sb_bt_control);
        lv_blue_list = (ListView) view.findViewById(R.id.lv_blue_list);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.init();
        initListener();
        initAdapter();
    }


    private void initAdapter() {
        bluetoothDevices = new ArrayList<>();
        blueListAdapter = new BlueListAdapter(getContext(), bluetoothDevices);
        lv_blue_list.setAdapter(blueListAdapter);
        lv_blue_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice bluetoothDevice = bluetoothDevices.get(position);
                mPresenter.connectBluetooth(bluetoothDevice.getAddress());
            }
        });
    }

    private void initListener() {
        sb_bt_control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.openOrCloseBlue(isChecked);
            }
        });
        srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("BlueFragment", "执行");
                mPresenter.searchBluetoothList();
            }
        });
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected BlueContract.IPrensenter createPresenter() {
        return new BluePresenter();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(getContext(), msg);
    }

    @Override
    public void controlBlueButton(boolean isOpen) {
        sb_bt_control.setCheckedNoEvent(isOpen);
        if (!isOpen) {
            if (bluetoothDevices != null && bluetoothDevices.size() > 0) {
                bluetoothDevices.clear();
                blueListAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void loadBtListSuccess(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevices.indexOf(bluetoothDevice) == -1) {
            blueListAdapter.addNewData(bluetoothDevice);
        }
    }

    @Override
    public void loadBtListFailure() {

    }


    @Override
    public void handleIntent() {

    }

    @Override
    public void controlSearchText(int state) {
        if (state == -1) {
            tv_bt_search_msg.setText("");
        } else if (state == 0) {
            tv_bt_search_msg.setText("搜索中...");
            srfl.setRefreshing(true);
        } else {
            tv_bt_search_msg.setText("搜索完成");
            srfl.setRefreshing(false);
        }
    }


    @Override
    public void updateBtList() {
        blueListAdapter.notifyDataSetChanged();
    }

    @Override
    public List<BluetoothDevice> getCurrentBtList() {
        return bluetoothDevices;
    }

    @Override
    public BlueListAdapter getBlueListAdapter() {
        return blueListAdapter;
    }


}
