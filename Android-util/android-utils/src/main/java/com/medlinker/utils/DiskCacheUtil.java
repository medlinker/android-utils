package com.medlinker.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.math.BigDecimal;

/**
 */
public class DiskCacheUtil {

    /** 默认缓存大小 */
    public static final String DEFAULT_CACHE_SIZE = "0M";

    private DiskCacheUtil() {
    }

    /**
     * 清除应用内部缓存(/data/data/com.xxx.xxx/cache)
     * @param context 应用上下文
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除应用数据库(/data/data/com.xxx.xxx/databases)
     * @param context 应用上下文
     */
    public static void cleanDatabase(Context context) {
        deleteFilesByDirectory(new File(Environment.getDataDirectory() + "/" + context.getPackageName() + "/databases"));
    }

    /**
     * 清除应用指定名称的数据库
     * @param context 应用上下文
     * @param dbName 数据库名称
     */
    public static void cleanDatabase(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     * @param context 应用上下文
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File(Environment.getDataDirectory() + "/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     * @param context 应用上下文
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     * @param context 应用上下文
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除自定义路径下的文件
     * @param filePath 文件目录
     */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 清除应用数据
     * @param context 应用上下文
     * @param filePaths 文件目录集
     */
    public static void cleanApplicationData(Context context, String... filePaths) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabase(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        if (filePaths == null) {
            return;
        }
        for (String filePath : filePaths) {
            cleanCustomCache(filePath);
        }
    }

    /**
     * 删除某个文件夹下的文件
     * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
        }
    }

    /**
     * 获取文件大小<br />
     * Context.getExternalFilesDir() --> SDCard/Android/data/应用的包名/files/目录，一般放一些长时间保存的数据<br />
     * Context.getExternalCacheDir() --> SDCard/Android/data/应用包名/cache/目录，一般存放临时缓存数据<br />
     * @param file 文件
     * @return
     * @throws Exception
     * @see Context#getExternalCacheDir()
     * @see Context#getExternalFilesDir(String)
    */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 判断是否为文件夹
                if (files[i].isDirectory()) {
                    size = size + getFolderSize(files[i]);
                } else {
                    size = size + files[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下文件及目录文件
     *
     * @param filePath 文件路径
     * @param isDelete 是否删除目录文件
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean isDelete) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        try {
            File file = new File(filePath);
            // 判断是否为文件夹
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
            }
            if (isDelete) {
                // 判断是否为文件夹
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    // 判断目录下是否有文件
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取缓存文件大小
     * @param file
     * @return
     * @throws Exception
     */
    public static String getCacheSize(File file) throws Exception {
        return getFormatSize(getFolderSize(file));
    }

    /**
     * 格式化单位
     * @param size 大小
     * @return 指定格式的字符串
     */
    public static String getFormatSize(long size,String defaultSizeStr) {
        if (size <= 0) {
            return defaultSizeStr;
        }
        double kiloByte = size / 1024;
        double megaByte = kiloByte / 1024;
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result = new BigDecimal(Double.toString(megaByte));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result = new BigDecimal(Double.toString(gigaByte));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
        }
        BigDecimal result = new BigDecimal(teraBytes);
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";
    }

    private static String doubleTrans(double d){
        if (d%1.0==0){
            // 小数是0
            return String.valueOf(Math.round(d));
        }
        return String.valueOf(d);
    }

    /**
     * 格式化单位
     * @param size 大小
     * @return 指定格式的字符串
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result = new BigDecimal(Double.toString(kiloByte));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result = new BigDecimal(Double.toString(megaByte));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result = new BigDecimal(Double.toString(gigaByte));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
        }
        BigDecimal result = new BigDecimal(teraBytes);
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";
    }

    /**
     * 清除WebView默认缓存
     * @param context 应用上下文
     */
    public static void clearWebCache(Context context) {
        if (context == null) {
            return;
        }
        context.getApplicationContext().deleteDatabase("webview.db");
        context.getApplicationContext().deleteDatabase("webviewCache.db");
    }

     //清除磁盘缓存
   /* public static void clearDiskCache() {
        // 清除图片磁盘缓存
        clearImageDiskCache();

        // 清除网络磁盘缓存
        clearNetworkCache();
    }*/

     //读取磁盘缓存
   /* public static String readDiskCache() {
        long imageCacheSize = readImageDiskCache();
        long networkCacheSize = readNetworkCache();
        try {
            return getFormatSize(imageCacheSize + networkCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEFAULT_CACHE_SIZE;
    }*/

    //**
     // 清除图片磁盘缓存
   /* public static void clearImageDiskCache() {
        try {
            Fresco.getImagePipeline().clearDiskCaches();
            Fresco.getImagePipelineFactory().getMainDiskStorageCache().clearAll();
            Fresco.getImagePipelineFactory().getSmallImageDiskStorageCache().clearAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

      //读取图片磁盘缓存大小
    /*private static long readImageDiskCache() {
        try {
            long mainSize = Fresco.getImagePipelineFactory().getMainDiskStorageCache().getSize();
            long smallSize = Fresco.getImagePipelineFactory().getSmallImageDiskStorageCache().getSize();
            return (mainSize + smallSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }*/

     //清除网络磁盘缓存
   /* public static void clearNetworkCache() {
        try {
            Api.getInstance().getRequestQueue().getCache().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

     //读取网络磁盘缓存大小
    /*
    private static long readNetworkCache() {
        final String DEFAULT_CACHE_DIR = "volley";
        try {
            File cacheDir = new File(MedlinkerApp.getApplication().getCacheDir(), DEFAULT_CACHE_DIR);
            return getFolderSize(cacheDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }*/
}
