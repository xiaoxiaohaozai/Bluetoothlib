package com.chenhao.bluetoothlib.bluetoothview;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.chenhao.bluetoothlib.R;
import com.chenhao.bluetoothlib.utils.ToastUtils;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhao on 2017/5/21.
 */

public class BlueFragment
        extends MVPBaseFragment<BlueContract.IView, BlueContract.IPrensenter>
        implements BlueContract.IView {


    private ImageView mBackView;
    private ImageButton mRefreshView;
    private TextView mDeviceName;
    private SwitchButton mBtSwitch;
    private View mLoadingView;
    private ListView mBlueList;
    private List<BluetoothDevice> bluetoothDevices;
    private BlueListAdapter blueListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bt_layout, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mBackView = (ImageView) view.findViewById(R.id.ib_back);
        mRefreshView = (ImageButton) view.findViewById(R.id.ib_refresh);
        mDeviceName = (TextView) view.findViewById(R.id.tv_device_name);
        mBtSwitch = (SwitchButton) view.findViewById(R.id.sb_bt);
        mLoadingView = view.findViewById(R.id.mk);
        mBlueList = (ListView) view.findViewById(R.id.lv_bluetooth);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.init();
        initListener();
        initAdapter();
        mPresenter.searchBluetoothList();//初始化查询
    }


    private void initAdapter() {
        bluetoothDevices = new ArrayList<>();
        blueListAdapter = new BlueListAdapter(getContext(), bluetoothDevices);
        mBlueList.setAdapter(blueListAdapter);
    }

    private void initListener() {
        //点击执行操作，自定义
        mBlueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.userDefined();
            }
        });
        //点击刷新
        mRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.searchBluetoothList();
            }
        });
        //打开蓝牙
        mBtSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.openOrCloseBlue(isChecked);
            }
        });
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                mPresenter.back();
            }
        });
    }

    @Override
    protected BlueContract.IPrensenter createPresenter() {
        return new BluePresenter();
    }


    @Override
    public void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showToast(getContext(), msg);
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(getContext(), msg);
    }

    @Override
    public void controlBlueButton(boolean isOpen) {
        mBtSwitch.setCheckedNoEvent(isOpen);
    }

    @Override
    public void foundSingleDevice(BluetoothDevice bluetoothDevice) {
        blueListAdapter.addNewData(bluetoothDevice);
        blueListAdapter.notifyDataSetChanged();
    }


    @Override
    public void handleIntent() {//自定一操作

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

    @Override
    public void setLocalDeviceName(String name) {
        mDeviceName.setText(name);
    }

    @Override
    public void handeleback() {
        //TODO 执行返回操作
    }

    @Override
    public void clearBtList() {
        if (bluetoothDevices != null && bluetoothDevices.size() > 0) {
            bluetoothDevices.clear();
            blueListAdapter.notifyDataSetChanged();
        }

    }


}
