package chao.app.ami;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;

import chao.app.ami.frames.FrameManager;
import chao.app.ami.launcher.drawer.DrawerManager;
import chao.app.ami.monitor.MonitorManager;
import chao.app.ami.proxy.ProxyManager;
import chao.app.ami.text.TextManager;
import chao.app.ami.utils.Util;
import chao.app.ami.viewinfo.InterceptorLayerManager;

/**
 * @author chao.qin
 * @since 2017/7/27
 *
 * 调试工具
 *
 */

public class Ami {

    private static final String TAG = "AMI";
    private static Application mApp;
    private static Ami mInstance;

    public static final int LIFECYCLE_LEVEL_FULL = 3;
    public static final int LIFECYCLE_LEVEL_SIMPLE = 2;
    public static final int LIFECYCLE_LEVEL_CREATE = 1;
    public static final int LIFECYCLE_LEVEL_NONE = 0;

    private static int mLifecycle = LIFECYCLE_LEVEL_NONE;

    private Ami(Application app) {
        mApp = app;
    }

    public static void init(Application app) {
        if (!Util.isHostAppDebugMode(app)) {
            return;
        }
        if (mInstance != null) {
            return;
        }

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
//                .penaltyDeath()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        mInstance = new Ami(app);
        ProxyManager.init(app);
        TextManager.init();
        FrameManager.init();
        MonitorManager.init(app);

        InterceptorLayerManager.init(false);
    }

    public static void setViewInterceptorEnabled(boolean enabled) {
        InterceptorLayerManager.get().setInterceptorEnabled(enabled);
    }

    /**
     *
     * @param drawerId 抽屉配置文件Id, 必须是R.raw.xxxx
     */
    public static void setDrawerId(int drawerId) {
        DrawerManager.init(getApp(), drawerId);
    }

    /**
     *  Ami 功能初始化
     *
     * @param app application
     * @param drawerId   抽屉配置文件Id, 必须是R.raw.xxxx
     *
     * @Deprecated 独立功能开关
     * @see #init(Application)
     * @see #setDrawerId(int)
     */
    @Deprecated
    public static void init(Application app, int drawerId) {
        if (!Util.isHostAppDebugMode(app)) {
            return;
        }
        if (mInstance != null) {
            return;
        }

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
//                .penaltyDeath()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        mInstance = new Ami(app);
        DrawerManager.init(app, drawerId);
        ProxyManager.init(app);
        TextManager.init();
        FrameManager.init();
    }

    public static void enableLeakCanary(Application app) {
        LeakCanary.install(app);
    }

    public static Application getApp() {
        return mApp;
    }

    public static void setLifecycleLevel(int level) {
        mLifecycle = level;
    }

    public static void log(String log) {
        log(TAG, log);
    }

    public static void deepLog(Object object) {
        if(object == null) {
            return;
        }
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder builder = new StringBuilder();
        builder.append(clazz.getSimpleName()).append("{");
        try {
            for (Field field: fields) {
                field.setAccessible(true);
                appendLog(object, field, builder);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        builder.append("}");
        Ami.log(builder.toString());
    }

    private static void appendLog(Object parent, Field field, StringBuilder logs) throws IllegalAccessException {

        Object obj = field.get(parent);
        logs.append(field.getName()).append(":");
        if (obj == null) {
            logs.append("null").append(", ");
        } else if (obj instanceof Number) {
            logs.append(obj).append(", ");
        } else if (obj instanceof String) {
            logs.append("\"").append(obj).append("\"").append(", ");
        } else if (obj.getClass() == Object.class) {
        } else if (obj.getClass().isArray()) {
            Object array[] = (Object[]) obj;
            logs.append(Arrays.toString(array)).append(", ");
        } else {
            appendLog(obj, field, logs);
        }
    }


    public static void log(String tag, String log) {

        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        String className = null;
        String method = null;
        for (StackTraceElement element: traces) {
            String name = element.getClassName();
            if (name.contains("dalvik") || name.contains("java.lang")) {
                continue;
            }
            if (!name.contains(Ami.class.getName()) && className == null) {
                className = element.getClassName();
                method = element.getMethodName();
                break;
            }

        }
        Log.d(tag, className + "." + method + "() >>> " + log);
    }

    public static void lifecycle(String tag, String log, int level) {
        if (mLifecycle == LIFECYCLE_LEVEL_NONE) {
            return;
        }
        if (level > mLifecycle) {
            return;
        }
        log(tag, log);
    }

}
