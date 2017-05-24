package com.chenhao.bluetoothlib.utils;

public class ByteUtils {
    public static byte sum(byte[] bytes) {
        byte b = 0;
        for (byte i : bytes) {
            b += i;
        }
        return b;
    }

    public static String toString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            int v = b & 0xFF;
            sb.append(Integer.toHexString(v))
                    .append(" ");
        }
        return sb.toString();
    }
}