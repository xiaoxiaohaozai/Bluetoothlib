package com.chenhao.bluetoothlib.entity;

/**
 * Created by chenhao on 2017/5/19.
 * 消息实体
 */

public class TypeMessage {
    public enum TYPE {
        STRING,
        BYTEARRAY
    }

    public String text;
    public byte[] data;

    public TYPE mTYPE;

    public TypeMessage(String text) {
        this.text = text;
        mTYPE = TYPE.STRING;
    }

    public TypeMessage(byte[] data) {
        this.data = data;
        mTYPE = TYPE.BYTEARRAY;
    }
}
