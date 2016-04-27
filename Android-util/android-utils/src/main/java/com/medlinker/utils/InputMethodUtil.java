package com.medlinker.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author <a href="mailto:tql@medlinker.net">tqlmorepassion</a>
 * @version 1.0
 * @description 输入法工具类
 * @time 2016/4/26 20:08
 */
public class InputMethodUtil {
    /**
     * 弹出输入法
     */
    public static void popInputMethod(final Context context, final View v, final int flags) {
        v.setFocusable(true);
        v.requestFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, flags);
            }
        }, 100);
    }

    public static void popInputMethod(final Context context, final View v) {
        popInputMethod(context, v, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏输入法
     * @param activity Activity
     */
    public static void hintInputMethod(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 隐藏输入法
     *
     * @param context  Context
     * @param editText EditText
     */
    public static void hideInputMethod(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}
