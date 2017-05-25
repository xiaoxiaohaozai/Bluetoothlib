package com.chenhao.bluetoothlib.bluetoothview;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.chenhao.bluetoothlib.R;

import java.util.List;

/**
 * Created by chenhao on 2017/4/15.
 * 蓝牙控制适配器
 */

public class BlueListAdapter extends BaseAdapter {


    private Context context;
    private List<BluetoothDevice> bluetooths;
    private final LayoutInflater inflater;

    public BlueListAdapter(Context context, List<BluetoothDevice> bluetooths) {
        this.context = context;
        this.bluetooths = bluetooths;
        inflater = LayoutInflater.from(context);
    }

    public void updateData(List<BluetoothDevice> newData) {
        this.bluetooths = newData;
        notifyDataSetChanged();
    }

    public void addNewData(BluetoothDevice newData) {
        bluetooths.add(newData);
        notifyDataSetChanged();
    }

    public List<BluetoothDevice> getBluetooths() {
        return bluetooths;
    }

    @Override
    public int getCount() {
        return bluetooths == null ? 0 : bluetooths.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return bluetooths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BluetoothDevice item = getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            //选择类型
            convertView = inflater.inflate(R.layout.item_blue, parent, false);
            holder.tv_blue_name = (TextView) convertView.findViewById(R.id.tv_blue_name);
            holder.iv_blue_icon = (ImageView) convertView.findViewById(R.id.iv_blue_icon);
            holder.tv_blue_address = (TextView) convertView.findViewById(R.id.tv_blue_address);
            holder.tv_blue_bond = (TextView) convertView.findViewById(R.id.tv_blue_bond);
            holder.tv_blue_connect = (TextView) convertView.findViewById(R.id.tv_blue_connect);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = item.getName();
        if (TextUtils.isEmpty(name)) {
            holder.tv_blue_name.setText("名称无法识别");
        } else {
            holder.tv_blue_name.setText(name);
        }
        holder.tv_blue_address.setText(item.getAddress());
        holder.tv_blue_connect.setText("未连接");
        holder.tv_blue_bond.setText("未配对");
        int bondState = item.getBondState(); //bond状态
        if (bondState == BluetoothDevice.BOND_BONDED) {
            holder.tv_blue_bond.setText("已配对");
        }
        holder.iv_blue_icon.setBackgroundResource(R.drawable.bt_icon_type_nomal);
        BluetoothClass bluetoothClass = item.getBluetoothClass();//蓝牙类
        if (bluetoothClass != null) {
            int deviceClass = bluetoothClass.getDeviceClass();//获得设备类型
            if (deviceClass == 524) {
                holder.iv_blue_icon.setBackgroundResource(R.drawable.bt_icon_type_phone);
            }
            if (deviceClass == 512) {
                holder.iv_blue_icon.setBackgroundResource(R.drawable.bt_icon_type_nomal);
            }
            if (deviceClass == 268) {
                holder.iv_blue_icon.setBackgroundResource(R.drawable.bt_icon_type_pc);
            }
        }
        if (currentedconnectDevice != null && currentedconnectDevice.getAddress().equals(item.getAddress())) {
            holder.tv_blue_connect.setText("已连接");
        }
        if (currentDisconnectDevice != null && currentDisconnectDevice.getAddress().equals(item.getAddress())) {
            holder.tv_blue_connect.setText("未连接");
        }

        return convertView;
    }

    private BluetoothDevice currentedconnectDevice;
    private BluetoothDevice currentDisconnectDevice;

    public void setCurrentConnectBt(BluetoothDevice bluetoothDevice) {
        currentedconnectDevice = bluetoothDevice;
    }

    public void setCurrentDisConnectBt(BluetoothDevice bluetoothDevice) {
        currentDisconnectDevice = bluetoothDevice;
    }


    private static class ViewHolder {
        ImageView iv_blue_icon;
        TextView tv_blue_name;
        TextView tv_blue_address;
        TextView tv_blue_bond;
        TextView tv_blue_connect;

    }
}
