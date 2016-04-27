package com.medlinker.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.File;

/**
 * @author <a href="mailto:tql@medlinker.net">tqlmorepassion</a>
 * @version 1.0
 * @description 功能描述
 * @time 2016/4/26 20:14
 */
public class PackageUtil {

    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static int getVersionCode(Context mContext) {
        int versionCode;
        try {
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getVersionName(Context mContext) {
        String versionName;
        try {
            versionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    public static boolean isInstalled(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
