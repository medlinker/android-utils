package com.medlinker.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public abstract class SoftKeyboardVisibleLayoutListenerImpl implements ViewTreeObserver.OnGlobalLayoutListener{

        private static final Rect RECT = new Rect();
        private final View v;

        public SoftKeyboardVisibleLayoutListenerImpl(View v) {
            this.v = v;
        }

        @Override
        public void onGlobalLayout() {
            v.getWindowVisibleDisplayFrame(RECT);
            int displayHeight = RECT.bottom - RECT.top;
            int height = v.getHeight();
            int keyboardHeight = height - displayHeight;
            boolean hide = (double) displayHeight / height > 0.8;
            onSoftKeyBoardChange(keyboardHeight, !hide);
            ViewCompatUtil.removeOnGlobalLayoutListener(v,this);
        }

        protected abstract void onSoftKeyBoardChange(int keyboardHeight, boolean b);
    }
