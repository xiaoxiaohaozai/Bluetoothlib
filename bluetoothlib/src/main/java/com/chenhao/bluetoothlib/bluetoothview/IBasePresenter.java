package com.chenhao.bluetoothlib.bluetoothview;


/**
 * Created by chenhao on 2017/4/14.
 * p的具备的基本方法
 */

public interface IBasePresenter<V> {
    void attachView(V view);


    void detachView();

}
