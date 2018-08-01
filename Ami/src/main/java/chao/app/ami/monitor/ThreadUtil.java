package chao.app.ami.monitor;

/**
 * Created by qinchao on 2017/9/7.
 */

public class ThreadUtil {

    private static final String MONITOR_PACKAGE_NAME = ThreadUtil.class.getName().replace(ThreadUtil.class.getSimpleName(), "");

    public static String threadString(Thread thread) {
        if (thread == null) {
            return "";
        }
        ThreadGroup group = thread.getThreadGroup();
        if (group != null) {
            return thread.getClass().getSimpleName() + "[" + thread.getName() + "," + thread.getId() + "," +
                    group.getName() + ", alive:" + thread.isAlive() + "]";
        } else {
            return thread.getClass().getSimpleName() + "[" + thread.getName() + "," + thread.getId() + "," +
                    ", alive:" + thread.isAlive() + "]";
        }
    }

    public static SourceInfo getSourceLocation(String... excludeList) {
        StackTraceElement[] stackElements = Thread.currentThread().getStackTrace();
        label:
        for(StackTraceElement element: stackElements) {
            String className = element.getClassName();
            if (Thread.class.getName().equals(className) || ThreadUtil.class.getName().equals(className)) {
                continue;
            }
            if (className.contains("dalvik.system.VMStack")) {
                continue;
            }
            if (element.toString().contains("_aroundBody1")) {//过滤aspectj
                continue;
            }
            if (element.toString().contains(MONITOR_PACKAGE_NAME)) {
                continue;
            }
            for (String excludeName: excludeList) {
                if (className.contains(excludeName)) {
                    continue label;
                }
            }
            return new SourceInfo(stackElements,element);
        }
        return new SourceInfo();
    }
}
