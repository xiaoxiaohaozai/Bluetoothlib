package com.chenhao.bluetoothlib.bluetoothview;

/**
 * Created by chenhao on 2017/4/14.
 * 每个view需要功能,进度条和错误信息显示
 */

public interface IBaseView {
    void showLoading();

    void hideLoading();

    void showError(String msg);

    void showToast(String msg);
}
