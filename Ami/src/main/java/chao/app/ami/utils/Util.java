package chao.app.ami.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import chao.app.ami.Ami;
import chao.app.ami.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author chao.qin
 * @since 2017/7/26
 */

public class Util implements Constants {

    private static final int BUFFER_SIZE = 1000;

    public static boolean isHostAppDebugMode(Application app) {
        String packageName = app.getPackageName();
        try {
            Class<?> buildConfig = Class.forName(packageName + ".BuildConfig");
            Field debugField = buildConfig.getDeclaredField("DEBUG");
            return (boolean) debugField.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    public static <T extends Collection> String toString(T collection) {
        return null;
    }

    public static String convert2Resource(Object obj, String defaultValue) {
        if (!(obj instanceof Integer)) {
            return defaultValue;
        }
        int resId = (int) obj;
        int packageId = (resId & 0x7f000000) >> 24;
        int resType = (resId & ~0x7f000000 & 0x00ff0000) >> 16;
        int id = resId & 0x0000ffff;

        if (resType == 0 || id == 0) {
            return defaultValue;
        }

        if (packageId == 0 || packageId > 0x7f) {
            return defaultValue;
        }

        String resName;
        Application app = Ami.getApp();
        Resources res = app.getResources();
        try {
            resName = res.getResourceName(resId);
            resName = resName.replaceAll(app.getPackageName(), "app");
        } catch (Throwable e) {
            return defaultValue;
        }

        return resName +
            "(" +
            resId +
            ")";
    }

    /**
     * 版本是否在Android6.0 以上
     */
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 是否在子进程中
     *
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context) {
        context = context.getApplicationContext();
        String processName = getProgressName(context);
        String packageName = context.getPackageName();
        return TextUtils.equals(processName, packageName);
    }

    /**
     * 取得当前进程的名称
     *
     * @param context
     * @return
     */
    public static String getProgressName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (mActivityManager != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        }
        return null;
    }

    private static HashMap<String, String> nameCache = new HashMap<>();

    public static String getThreadName(String pid) {
        String name = nameCache.get(pid);
        if (name != null) {
            return name;
        }
        File statFile = new File("/proc/" + pid + "/stat");
        if (!statFile.exists()) {
            return String.valueOf(pid);
        }
        try {
            BufferedReader pidReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(statFile)), BUFFER_SIZE);

            String line = pidReader.readLine();
            if (line != null) {
                // 找到第一个'('和最后一个')'
                int firstQianKuoHao = 0;
                int lastHouKuoHao = 0;
                for (int k = 0; k < line.length(); k++) {
                    if (line.charAt(k) == '(' && firstQianKuoHao == 0) {
                        firstQianKuoHao = k;
                    }

                    if (line.charAt(k) == ')' && k > lastHouKuoHao) {
                        lastHouKuoHao = k;
                    }
                }
                name = line.substring(firstQianKuoHao + 1,lastHouKuoHao);
                nameCache.put(pid, name);
                return name;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return String.valueOf(pid);
    }
}
