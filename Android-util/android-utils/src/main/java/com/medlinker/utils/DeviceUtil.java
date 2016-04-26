package com.medlinker.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.text.Selection;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.widget.EditText;

import java.util.List;

public class DeviceUtil {

    private static int STATUSBAR_HEIGHT = 0;
    private static DisplayMetrics mDisplayMetrics;

    public static DisplayMetrics getDisplayMetrics(Context context) {
        if (null == mDisplayMetrics) {
            mDisplayMetrics = context.getResources().getDisplayMetrics();
        }
        return mDisplayMetrics;
    }

    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }
    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    public static int sp2px(Context context,float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context,int pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dip2px(Context context,float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * get the status bar height
     */
    public static int getStatusBarHeight(Activity activity) {
        if (STATUSBAR_HEIGHT > 0) {
            return STATUSBAR_HEIGHT;
        }
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;
        if (statusHeight <= 0) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
                STATUSBAR_HEIGHT = statusHeight;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }


    /**
     * 设置输入框光标位置到末尾
     * @param editText EditText
     */
    public static void setTextCursor(EditText editText) {
        CharSequence text = editText.getText();
        if (text != null) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    /**
     * 记录上次点击时间
     */
    private static long mLastClickTime;

    /**
     * 判断是否快速点击
     */
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
     * 获取设备堆内存大小
     * @param context 应用上下文
     */
    public static int getMemoryClass(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return manager.getMemoryClass();
    }

    /**
     * create the shortcut
     * @param cls the activity to trigger while user click the shortcut
     * @param iconId  the icon of shortcut
     * @param shortcutName the display name of shortcut
     * @param duplicate  duplicate to create more
     */
    public static void createShortCut(Context context, Class<?> cls,int iconId, String shortcutName , boolean duplicate) {
        if (context == null || isShortcutExist(context,shortcutName)) {
            return;
        }
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        Intent intent = new Intent(context, cls);
        // 设置这两个属性当应用程序卸载时桌面上的快捷方式会删除
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, iconId);
        shortcutIntent.putExtra("duplicate", duplicate);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

        context.sendBroadcast(shortcutIntent);
    }

    /**
     * 删除快捷方式
     */
    public static void deleteShortcut(Context activity, String shortcutName) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        /**改成以下方式能够成功删除，估计是删除和创建需要对应才能找到快捷方式并成功删除**/
        Intent intent = new Intent();
        intent.setClass(activity, activity.getClass());
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        activity.sendBroadcast(shortcut);
    }

    /**
     * 判断快捷方式是否已存在
     *
     * @param context 应用上下文
     * @return 快捷方式是否已存在
     */
    private static boolean isShortcutExist(Context context,String shortcutName) {
        String providerAuthority = null;
        try {
            providerAuthority = readProviderAuthority(context, "com.android.launcher.permission.READ_SETTINGS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (providerAuthority == null) {
            return true;
        }
        ContentResolver cr = context.getContentResolver();
        Uri contentUri = Uri.parse("content://" + providerAuthority + "/favorites?notify=true");
        Cursor cursor;
        try {
            cursor = cr.query(contentUri, null, "title=?", new String[]{ shortcutName }, null);
        } catch (Exception e) {
            return true;
        }
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }

    /**
     * 读取权限
     *
     * @param context 应用上下文
     * @param permission 应用权限
     * @return
     */
    private static String readProviderAuthority(Context context, String permission) {
        if (permission == null) {
            return null;
        }
        List<PackageInfo> packageInfoList = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
        if (packageInfoList == null) {
            return null;
        }
        for (PackageInfo pack : packageInfoList) {
            ProviderInfo[] providers = pack.providers;
            if (providers != null) {
                for (ProviderInfo provider : providers) {
                    if (permission.equals(provider.readPermission)) {
                        return provider.authority;
                    } else if (permission.equals(provider.writePermission)) {
                        return provider.authority;
                    }
                }
            }
        }
        return null;
    }

}
