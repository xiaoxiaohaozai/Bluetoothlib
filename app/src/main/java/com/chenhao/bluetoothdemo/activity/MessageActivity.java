package com.chenhao.bluetoothdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenhao.bluetoothdemo.MessageContract;
import com.chenhao.bluetoothdemo.MessagePresenter;
import com.chenhao.bluetoothdemo.R;
import com.chenhao.bluetoothdemo.adapter.MessageAdapter;
import com.chenhao.bluetoothdemo.bean.Msg;
import com.chenhao.bluetoothlib.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhao on 2017/5/25.
 */

public class MessageActivity
        extends MvpBaseActivity<MessageContract.IView, MessageContract.IPresenter>
        implements MessageContract.IView {

    private TextView tv_msg;
    private Button bt_send;
    private EditText et_input;
    private ListView lv_chat;
    private List<Msg> msgs;
    private MessageAdapter messageAdapter;

    public static void showMessageActivity(Context context, boolean isClient, String address) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra("isClient", isClient);
        intent.putExtra("address", address);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_layout);
        initViews();
        initAdapter();
        initListener();
        Intent intent = getIntent();
        //当前是服务器还是客户端
        mPresenter.asClientOrServer(intent.getBooleanExtra("isClient", true), intent.getStringExtra("address"));
    }

    private void initAdapter() {
        msgs = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, msgs);
        lv_chat.setAdapter(messageAdapter);
    }

    private void initListener() {
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.sendMsg();
            }
        });
    }

    private void initViews() {
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        bt_send = (Button) findViewById(R.id.bt_send);
        et_input = (EditText) findViewById(R.id.et_input);
        lv_chat = (ListView) findViewById(R.id.lv_chat);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {
        ToastUtils.showNoUIThread(this, msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showNoUIThread(this, msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void showSendMsg(final String s) {
        lv_chat.post(new Runnable() {
            @Override
            public void run() {
                messageAdapter.addNewData(new Msg(s, 1, "发送了:"));
            }
        });
    }

    @Override
    public void showReceiveMsg(final String s) {
        lv_chat.post(new Runnable() {
            @Override
            public void run() {
                messageAdapter.addNewData(new Msg(s, 0, "收到了:"));
            }
        });

    }

    @Override
    public String getInputMsg() {
        return et_input.getText().toString().trim();
    }

    @Override
    public void showTitleMsg(final String s) {
        tv_msg.post(new Runnable() {
            @Override
            public void run() {
                tv_msg.setText(s);
            }
        });
        mPresenter.openReceiveService();

    }

    @Override
    public MessageContract.IPresenter createPresenter() {
        return new MessagePresenter();
    }
}
