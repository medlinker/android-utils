package com.medlinker.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.File;

/**
 * @author <a href="mailto:tql@medlinker.net">tqlmorepassion</a>
 * @version 1.0
 * @description 功能描述
 * @time 2016/4/26 20:14
 */
public class PackageUtil {

    /**
     * 获取SDK版本
     *
     * @return
     */
    public static int getSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {

        }
        return version;
    }

    /**
     * 获取版本code
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = -1;
        try {
            versionCode = mContext.getPackageManager().getPackageInfo(getPackageName(mContext), 0).versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取版本名字
     *
     * @param mContext
     * @return
     */
    public static String getVersionName(Context mContext) {
        String versionName = "";
        try {
            versionName = mContext.getPackageManager().getPackageInfo(getPackageName(mContext), 0).versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 得到包名
     *
     * @param mContext
     * @return
     */
    public static String getPackageName(Context mContext) {
        if (mContext != null) {
            return mContext.getPackageName();
        }
        return "net.medlinker.medlinker";
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
