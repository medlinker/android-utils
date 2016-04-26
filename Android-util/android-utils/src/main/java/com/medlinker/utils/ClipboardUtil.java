package com.medlinker.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

/**
 * Created by heaven7 on 2016/4/26.
 */
public class ClipboardUtil {

    private static final  String DEFAULT_LABEL = "";
    /**
     * copy the text to clipboard and replace the previous clip.
     */
    public static void updateTextToClicpboard(Context context, CharSequence text) {
        updateTextToClicpboard(context,DEFAULT_LABEL,text);
    }
    /**
     * copy the text to clipboard and replace the previous clip.
     * @param label the label of clip
     * @param text the text
     */
    public static void updateTextToClicpboard(Context context, CharSequence label , CharSequence text) {
        if (null == context || TextUtils.isEmpty(text)) {
            return;
        }
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text));
    }

    /**
     * add the text to clipboard (default label )and return the text's index of clip .
     * @param text the text
     * @return the text's index of this clip
     */
    public static int addTextToClicpboard(Context context,  CharSequence text) {
        return addTextToClicpboard(context, DEFAULT_LABEL , text);
    }
    /**
     * add the text to clipboard and return the text's index of clip .
     * @param label the label of clip, only use when have no clip data .
     * @param text the text
     * @return the text's index of this clip
     */
    public static int addTextToClicpboard(Context context, CharSequence label , CharSequence text) {
        if (null == context || TextUtils.isEmpty(text)) {
            return -1;
        }
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = manager.getPrimaryClip();
        if(clip == null) {
            manager.setPrimaryClip(ClipData.newPlainText(label, text));
            return 0;
        }else{
            clip.addItem(new ClipData.Item(text));
            return clip.getItemCount() - 1;
        }
    }

    public static ClipData.Item getPrimaryClip(Context context){
        return getPrimaryClip(context,0);
    }
    public static ClipData.Item getPrimaryClip(Context context, int index){
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = manager.getPrimaryClip();
        if(clip == null) return null;
        return  clip.getItemCount()>0 ? clip.getItemAt(index) : null;
    }
    /**
     * @return the text of target (index = 0 ) 's clip data.
     */
    public static CharSequence getPrimaryClipText(Context context){
        final ClipData.Item item = getPrimaryClip(context);
        if(item == null){
            return null;
        }
        return item.getText();
    }

    /**
     * @param index the index of text clip text
     * @return the text of target index's clip data.
     */
    public static CharSequence getPrimaryClipText(Context context,int index){
        final ClipData.Item item = getPrimaryClip(context,index);
        if(item == null){
            return null;
        }
        return item.getText();
    }

}
