package com.medlinker.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public abstract class SoftKeyboardLayoutListenerImpl implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final Rect RECT = new Rect();
    private final View v;

    public SoftKeyboardLayoutListenerImpl(View v) {
        this.v = v;
    }

    @Override
    public void onGlobalLayout() {
        v.getWindowVisibleDisplayFrame(RECT);
        int displayHeight = RECT.bottom - RECT.top;
        int height = v.getHeight();
        int keyboardHeight = height - displayHeight;
        boolean hide = (double) displayHeight / height > 0.8;
        onSoftKeyboardCallback(keyboardHeight, !hide);
        ViewCompatUtil.removeOnGlobalLayoutListener(v, this);
    }

    /**
     * @param keyboardHeight the height of keyboard
     * @param visible        is the keyboard visible
     */
    protected abstract void onSoftKeyboardCallback(int keyboardHeight, boolean visible);
}
