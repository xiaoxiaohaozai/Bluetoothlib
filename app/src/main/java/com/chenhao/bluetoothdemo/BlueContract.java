package com.chenhao.bluetoothdemo;

import android.bluetooth.BluetoothDevice;

import com.chenhao.bluetoothdemo.adapter.BlueListAdapter;
import com.chenhao.bluetoothdemo.base.IBasePresenter;
import com.chenhao.bluetoothdemo.base.IBaseView;

import java.util.List;

/**
 * Created by chenhao on 2017/5/21.
 */

public class BlueContract {
    interface IView extends IBaseView {

        void controlBlueButton(boolean isOpen);//控制蓝牙按钮状态

        void loadBtListSuccess(BluetoothDevice bluetoothDevice);

        void loadBtListFailure();

        void handleIntent();//跳转

        void controlSearchText(int state);//-1 0 1

        void updateBtList();//更新列表

        List<BluetoothDevice> getCurrentBtList();

        BlueListAdapter getBlueListAdapter();

    }

    interface IPrensenter extends IBasePresenter<IView> {
        void init();//初始化配置

        void openOrCloseBlue(boolean isOpen);

        void searchBluetoothList();

        void connectBluetooth(String adress);

    }
}
