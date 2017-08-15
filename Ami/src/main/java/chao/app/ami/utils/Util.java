package chao.app.ami.utils;

import android.app.Application;
import android.content.res.Resources;

import java.lang.reflect.Field;
import java.util.Collection;

import chao.app.ami.Ami;
import chao.app.ami.Constants;

/**
 * @author chao.qin
 * @since 2017/7/26
 */

public class Util implements Constants{

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

    public static <T extends Collection> String toString(T collection) {
        return null;
    }

    public static String convert2Resource(Object obj, String defaultValue) {
        if (!(obj instanceof Integer)) {
            return defaultValue;
        }
        int resId = (int) obj;
        int packageId = (resId & 0x7f000000) >> 24;
        int resType   = (resId & ~0x7f000000 & 0x00ff0000) >> 16;
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
}
