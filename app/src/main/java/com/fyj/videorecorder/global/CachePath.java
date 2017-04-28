package com.fyj.videorecorder.global;

import android.content.Context;
import android.os.Environment;

import com.fyj.videorecorder.util.StorageUtils;

import java.io.File;

/**
 * 当前作者: Fyj<br>
 * 时间: 2016/9/8<br>
 * 邮箱: f279259625@gmail.com<br>
 * 修改次数: <br>
 * 描述: 全局的缓存路径
 */
public class CachePath {

    private static String dirName = "video";
    //目录名字
    private static final String CACHE_EXTRAL_FILE_NAME_VEDIO_COMPRESS = "video";
    //内存缓存路径
    private static String CACHE_EXTRAL_FILE_VEDIO = "";

    /**
     * 重命名主文件夹名字
     *
     * @param name 名字
     */
    public static void initDirName(String name) {
        if (!isEmpty(name)) {
            dirName = name;
        }
    }

    /**
     * 创建路径
     *
     * @param context    上下文
     * @param extralPath 路径
     * @param name       名字
     * @return 全路径
     */
    private static String CreateCachePath(Context context, String extralPath, String name) {
        String path = "";
        if (isSDCardEnable()) {
            String tempPath = (isEmpty(extralPath) ? getSDCardPath() : extralPath) + name;
            File dir = new File(tempPath);
            if (!dir.exists()) {
                boolean mkdir = dir.mkdirs();
                if (mkdir) {
                    path = tempPath;
                } else {
                    path = StorageUtils.getCacheDirectory(context, name).getAbsolutePath();
                }
            } else {
                path = tempPath;
            }
        } else {
            path = StorageUtils.getCacheDirectory(context, name).getAbsolutePath();
        }
        path = path + File.separator;
        return path;
    }

    /**
     * 创建路径
     *
     * @param context 上下文
     * @param dirName 路径
     * @param name    名字
     * @return 全路径
     */
    public static String getCachePath(Context context, String dirName, String name) {
        String pathTotal = CreateCachePath(context, "", dirName);
        String path = "";
        path = CreateCachePath(context, pathTotal, name);
        return path;
    }

    /**
     * 创建路径
     *
     * @param context 上下文
     * @param name    名字
     * @return 全路径
     */
    public static String getCachePath(Context context, String name) {
        String pathTotal = CreateCachePath(context, "", dirName);
        String path = "";
        path = CreateCachePath(context, pathTotal, name);
        return path;
    }

    /**
     * 返回全路径
     *
     * @param context 上下文
     * @param value   路径
     * @param name    名字
     * @return 全路径
     */
    private static String returnValue(Context context, String value, String name) {
        if (isEmpty(value)) {
            return value = getCachePath(context, name);
        } else {
            return value;
        }
    }


//    /**
//     * 获取视频存放文件夹地址
//     * 在data/data下
//     *
//     * @param context 上下文
//     * @return 地址
//     */
//    public static String getMediaCachePath(Context context) {
//
//        if (isEmpty(CACHE_EXTRAL_FILE_VEDIO)) {
//            String pathTotal = CreateCachePath(context, context.getCacheDir().getAbsolutePath() + "/", dirName);
//            return CACHE_EXTRAL_FILE_VEDIO = CreateCachePath(context, pathTotal, CACHE_EXTRAL_FILE_NAME_VEDIO_COMPRESS);
//        } else {
//            return CACHE_EXTRAL_FILE_VEDIO;
//        }
//    }

    /**
     * 获取视频存放文件夹地址
     * 在SD下
     *
     * @param context 上下文
     * @return 地址
     */
    public static String getMediaCachePath(Context context) {
        return returnValue(context, CACHE_EXTRAL_FILE_VEDIO, CACHE_EXTRAL_FILE_NAME_VEDIO_COMPRESS);
    }


    /**
     * http请求结果存放文件夹地址
     * 在data/data下
     *
     * @param context 上下文
     * @return 地址
     */
    public static File getRetrofitCachePath(Context context) {

        return context.getCacheDir();
    }

    private static boolean isEmpty(String str) {
        return (null == str || "".equals(str.trim()) || "null".equals(str));
    }

    /**
     * 获取设备SD卡是否可用
     *
     * @return true : 可用<br>false : 不可用
     */
    private static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取设备SD卡路径
     * <p>一般是/storage/emulated/0/</p>
     *
     * @return SD卡路径
     */
    private static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

}
