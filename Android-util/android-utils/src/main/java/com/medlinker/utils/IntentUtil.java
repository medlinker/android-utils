package com.medlinker.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * @author <a href="mailto:tql@medlinker.net">tqlmorepassion</a>
 * @version 1.0
 * @description 意图工具类。常用的相关跳转
 * @time 2016/4/26 20:11
 */
public class IntentUtil {

    /**
     * 拨打电话
     */
    public static void call(Context context, String phoneNum) {
        Uri uri = Uri.parse("tel:" + phoneNum);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转到短信发送界面
     *
     * @param context    上下文
     * @param smsContent 短信内容
     */
    public static void smsTo(Context context, String smsContent) {
        smsTo(context, smsContent, "");
    }

    /**
     * 跳转到短信发送界面
     *
     * @param context     上下文
     * @param smsContent  短信内容
     * @param phoneNumber 联系人号码
     */
    public static void smsTo(Context context, String smsContent, String phoneNumber) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("sms_body", smsContent);
        intent.setType("vnd.android-dir/mms-sms");
        intent.setData(uri);
        try {
            // 跳转到短信发送界面，此处需要捕捉异常
            context.startActivity(intent);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 跳转到相关的应用程序
     * @param context 上下文
     * @param url     链接地址
     */
    public static void toUrl(Context context, String url) {
        if (context == null || TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * 跳转到邮件发送界面
     *
     * @param context      上下文
     * @param emailAddress 邮箱地址
     */
    public static void toEmail(Context context, String emailAddress) {
        if (context == null || TextUtils.isEmpty(emailAddress)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailAddress));
        context.startActivity(intent);
    }
}
