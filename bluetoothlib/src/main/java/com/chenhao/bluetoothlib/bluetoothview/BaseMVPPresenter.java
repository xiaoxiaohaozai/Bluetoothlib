package com.chenhao.bluetoothlib.bluetoothview;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by chenhao on 2017/4/14.
 * 避免内存泄漏
 */

public abstract class BaseMVPPresenter<V extends IBaseView> implements IBasePresenter<V> {
    public Reference<V> mViewRef;//声明一个弱引用，防止内存泄漏

    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public V getView() {
        return mViewRef.get();
    }

    /**
     * get()可能返回空，在执行getview（）之前必须先判断
     *
     * @return
     */
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }
}
