package com.chenhao.bluetoothdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chenhao.bluetoothdemo.R;
import com.chenhao.bluetoothdemo.bean.Msg;


import java.util.List;


/**
 * Created by chenhao on 2017/5/25.
 */

public class MessageAdapter extends BaseAdapter {

    private Context context;
    private List<Msg> msgs;
    private final LayoutInflater inflater;

    public MessageAdapter(Context context, List<Msg> msgs) {
        this.context = context;
        this.msgs = msgs;
        inflater = LayoutInflater.from(context);
    }

    public void addNewData(Msg msg) {
        msgs.add(msg);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return msgs == null ? 0 : msgs.size();
    }

    @Override
    public Msg getItem(int position) {
        return msgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Msg item = getItem(position);
        int type = getItemViewType(position);
        ViewHolder holder = null;
        if (convertView == null) {
            if (type == 0) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_received_layout, null);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name_in);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_msg_in);
            } else {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.ite_send_layout, null);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name_out);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_msg_out);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(item.name);
        holder.tv_content.setText(item.content);

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return msgs.get(position).type;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private static class ViewHolder {
        TextView tv_name;
        TextView tv_content;
    }
}
