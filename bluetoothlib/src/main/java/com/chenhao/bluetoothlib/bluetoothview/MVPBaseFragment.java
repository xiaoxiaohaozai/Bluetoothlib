package com.chenhao.bluetoothlib.bluetoothview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by chenhao on 2017/4/14.
 * mvpfragment 基类
 */

public abstract class MVPBaseFragment<V extends IBaseView, T extends IBasePresenter<V>> extends Fragment {
    protected T mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = createPresenter();//创建presenter
        mPresenter.attachView((V) this);//绑定presenter
    }

    protected abstract T createPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();//解除绑定
    }
}
