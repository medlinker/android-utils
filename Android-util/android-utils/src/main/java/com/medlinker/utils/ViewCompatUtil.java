package com.medlinker.utils;

import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by heaven7 on 2016/4/27.
 */
public class ViewCompatUtil {

    public static void removeOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener l) {
        if(VersionUtils.hasJellyBean()){
            view.getViewTreeObserver().removeOnGlobalLayoutListener(l);
        }else{
            view.getViewTreeObserver().removeGlobalOnLayoutListener(l);
        }
    }
}
