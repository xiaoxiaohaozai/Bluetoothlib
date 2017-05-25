package com.chenhao.bluetoothlib.view.base;

/**
 * Created by chenhao on 2017/5/25.
 */

public interface IBasePresenter<V> {
    void attachView(V view);
    
    void detachView();
}
