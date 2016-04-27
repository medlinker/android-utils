package com.medlinker.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author <a href="mailto:tql@medlinker.net">tqlmorepassion</a>
 * @version 1.0
 * @description 功能描述
 * @time 2016/4/26 20:13
 */
public class NetworkUtil {

    /**
     * 判断网络是否可用
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo[] networkInfoList = cm.getAllNetworkInfo();
        if (networkInfoList != null) {
            for (int i = 0; i < networkInfoList.length; i++) {
                // 判断网络是否已连接
                if (networkInfoList[i].getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断WIFI是否连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo.State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        return wifiState == NetworkInfo.State.CONNECTED;
    }
}
