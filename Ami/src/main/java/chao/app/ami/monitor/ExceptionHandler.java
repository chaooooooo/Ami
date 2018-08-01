package chao.app.ami.monitor;

/**
 * Created by qinchao on 2017/9/29.
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mRawUncaughtExceptionHandler;

    public ExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        mRawUncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        ThreadMonitor.instance().appendFile("!!!uncaughtException dump!!!", false);
        ThreadMonitor.instance().dumpThreadInfo();

        if (mRawUncaughtExceptionHandler != null) {
            mRawUncaughtExceptionHandler.uncaughtException(t, e);
        }
    }
}
