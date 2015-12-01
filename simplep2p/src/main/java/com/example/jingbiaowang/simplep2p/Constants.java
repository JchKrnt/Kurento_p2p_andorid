package com.example.jingbiaowang.simplep2p;

/**
 * Created by jingbiaowang on 2015/11/25.
 */
public class Constants {


    public static final String WS_HOST = "ws://10.2.163.14";

    public static final String WS_PORT = "8080";

    public static final String WS_METHOD = "call";

    public static String getWSUrl() {

        return WS_HOST + ":" + WS_PORT + '/' + WS_METHOD;

    }
}
