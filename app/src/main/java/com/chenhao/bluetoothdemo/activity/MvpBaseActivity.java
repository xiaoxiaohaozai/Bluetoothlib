package com.chenhao.bluetoothdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.chenhao.bluetoothlib.view.base.IBasePresenter;
import com.chenhao.bluetoothlib.view.base.IBaseView;

/**
 * Created by chenhao on 2017/5/25.
 */

public abstract class MvpBaseActivity<V extends IBaseView, T extends IBasePresenter<V>>
        extends AppCompatActivity {

    public T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    public abstract T createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
