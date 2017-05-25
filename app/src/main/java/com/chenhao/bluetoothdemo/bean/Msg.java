package com.chenhao.bluetoothdemo.bean;

/**
 * Created by chenhao on 2017/5/25.
 * 消息实体类
 */

public class Msg {

    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;
    public String content;
    public int type;
    public String name;

    public Msg() {
    }

    public Msg(String content, int type, String name) {
        this.content = content;
        this.type = type;
        this.name = name;
    }
}
