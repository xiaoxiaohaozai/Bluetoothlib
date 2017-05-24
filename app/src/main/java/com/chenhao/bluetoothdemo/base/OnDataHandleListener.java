package com.chenhao.bluetoothdemo.base;

/**
 * Created by chenhao on 2017/4/14.
 * model 结果
 */

public interface OnDataHandleListener<T> {
    void onSuccess(T result);

    void onFailure(T result, String msg);
}
