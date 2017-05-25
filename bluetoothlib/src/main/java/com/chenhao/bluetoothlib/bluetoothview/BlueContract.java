package com.chenhao.bluetoothlib.bluetoothview;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * Created by chenhao on 2017/5/21.
 */

public class BlueContract {
    interface IView extends IBaseView {

        void controlBlueButton(boolean isOpen);//控制蓝牙按钮状态

        void foundSingleDevice(BluetoothDevice bluetoothDevice);

        void handleIntent();//跳转

        void updateBtList();//更新列表

        List<BluetoothDevice> getCurrentBtList();

        BlueListAdapter getBlueListAdapter();

        void setLocalDeviceName(String name);

        void handeleback();

        void clearBtList();
    }

    interface IPrensenter extends IBasePresenter<IView> {
        void init();//初始化配置

        void openOrCloseBlue(boolean isOpen);

        void searchBluetoothList();

        void connectBluetooth(String adress);

        void userDefined();//用户自定义操作

        void back();
    }
}
