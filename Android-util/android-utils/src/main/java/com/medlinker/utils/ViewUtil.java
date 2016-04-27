package com.medlinker.utils;

import android.content.Context;
import android.support.annotation.IdRes;
import android.text.Selection;
import android.text.Spannable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

/**
 * @author <a href="mailto:tql@medlinker.net">tqlmorepassion</a>
 * @version 1.0
 * @description 界面组件的工具类集合
 * @time 2016/4/26 19:45
 */
public class ViewUtil {

    /**
     * 设置输入框光标位置到末尾
     * @param editText EditText
     */
    public static void setEditTextCursor(EditText editText) {
        CharSequence text = editText.getText();
        if (text != null) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    /**
     * 判断是否快速点击
     */
    private static long mLastClickTime;

    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        long distTime = time - mLastClickTime;
        if (0 < distTime && distTime < 600) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }

    /**
     * 注入依附的view
     *
     * @param context
     * @param target  目标view
     * @param achor   依附View
     * @param resId   依附view的ID
     * @param width   长宽 ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT 具体值
     * @return
     */
    public static FrameLayout injectAchorView(Context context, View target, View achor, @IdRes int resId, int width, int height) {
        FrameLayout frameLayout = null;
        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.gravity = Gravity.CENTER;
        achor.setLayoutParams(params);
        achor.setId(resId);
        if (target.getParent() instanceof FrameLayout) {
            frameLayout = (FrameLayout) target.getParent();
            frameLayout.addView(achor);
        } else if (target.getParent() instanceof ViewGroup) {
            ViewGroup parentContainer = (ViewGroup) target.getParent();
            int groupIndex = parentContainer.indexOfChild(target);
            parentContainer.removeView(target);
            frameLayout = new FrameLayout(context);
            ViewGroup.LayoutParams parentLayoutParams = target.getLayoutParams();
            frameLayout.setLayoutParams(parentLayoutParams);
            target.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            parentContainer.addView(frameLayout, groupIndex, parentLayoutParams);
            frameLayout.addView(target);
            frameLayout.addView(achor);
        }
        return frameLayout;
    }

}
