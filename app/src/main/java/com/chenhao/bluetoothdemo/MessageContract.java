package com.chenhao.bluetoothdemo;

import com.chenhao.bluetoothlib.view.base.IBasePresenter;
import com.chenhao.bluetoothlib.view.base.IBaseView;

/**
 * Created by chenhao on 2017/5/25.
 */

public class MessageContract {
   public interface IView extends IBaseView {
        void showSendMsg(String s);

        void showReceiveMsg(String s);

        String getInputMsg();//获得输入的信息

        void showTitleMsg(String s);
    }

   public interface IPresenter extends IBasePresenter<IView> {
        void sendMsg();

        void asClientOrServer(boolean isClient, String address);

        void openReceiveService();

    }

}
