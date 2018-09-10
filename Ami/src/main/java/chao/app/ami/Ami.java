package chao.app.ami;

import android.app.Application;
import android.util.Log;
import chao.app.ami.base.AmiHandlerThread;
import chao.app.ami.frames.FrameManager;
import chao.app.ami.launcher.drawer.DrawerManager;
import chao.app.ami.proxy.ProxyManager;
import chao.app.ami.text.TextManager;
import chao.app.ami.utils.Util;
import chao.app.ami.viewinfo.InterceptorLayerManager;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author chao.qin
 * @since 2017/7/27
 *
 * 调试工具
 *
 */

public class Ami {

    public static final int DEBUG_MODE_ENABLED = 1;

    public static final int DEBUG_MODE_DISABLED = -1;

    public static final int DEBUG_MODE_UNSET = 0;

    private static final String TAG = "AMI";
    private static Application mApp;
    private static Ami mInstance;

    private static AmiHandlerThread sHandlerThread;

    private static int mDebugMode = DEBUG_MODE_UNSET;

    public static final int LIFECYCLE_LEVEL_FULL = 3;
    public static final int LIFECYCLE_LEVEL_SIMPLE = 2;
    public static final int LIFECYCLE_LEVEL_CREATE = 1;
    public static final int LIFECYCLE_LEVEL_NONE = 0;

    private static int mLifecycle = LIFECYCLE_LEVEL_NONE;

    private Ami(Application app) {
        mApp = app;
    }

    private static boolean isDebugMode(Application app) {
        if (mDebugMode == DEBUG_MODE_UNSET) {
            boolean enabled = Util.isApkDebugable(app);
            mDebugMode = enabled ? DEBUG_MODE_ENABLED: DEBUG_MODE_DISABLED;
        }
        return mDebugMode == DEBUG_MODE_ENABLED;
    }

    public static void setDebugMode(int debugMode) {
        mDebugMode = debugMode;
    }

    public static void init(Application app) {
        if (!isDebugMode(app)) {
            return;
        }
        if (mInstance != null) {
            return;
        }

        mInstance = new Ami(app);
        ProxyManager.init(app);
        TextManager.init();
        FrameManager.init();
//        MonitorManager.init(app);

        InterceptorLayerManager.init(false);


        sHandlerThread = new AmiHandlerThread();
        sHandlerThread.start();
    }

    public static boolean inited() {
        return mInstance != null;
    }

    public static void setViewInterceptorEnabled(boolean enabled) {
        if (!isDebugMode(mApp)) {
            return;
        }
        InterceptorLayerManager.get().setInterceptorEnabled(enabled);
    }

    /**
     *
     * @param drawerId 抽屉配置文件Id, 必须是R.raw.xxxx
     */
    public static void setDrawerId(int drawerId) {
        if (!isDebugMode(mApp)) {
            return;
        }
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
        if (!isDebugMode(app)) {
            return;
        }
        if (mInstance != null) {
            return;
        }

        mInstance = new Ami(app);
        DrawerManager.init(app, drawerId);
        ProxyManager.init(app);
        TextManager.init();
        FrameManager.init();
    }


    public static Application getApp() {
        return mApp;
    }

    public static void setLifecycleLevel(int level) {
        mLifecycle = level;
    }

    public static void log(Object log) {
        log(TAG, " >>> " + String.valueOf(log));
    }

    public static void log() {
        log(TAG, "");
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
            if (!name.contains(Ami.class.getName())) {
                className = element.getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                method = element.getMethodName();
                break;
            }

        }
        Log.d(tag, className + "." + method + "() " + log);
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

    public static AmiHandlerThread getHandlerThread() {
        return sHandlerThread;
    }
}
