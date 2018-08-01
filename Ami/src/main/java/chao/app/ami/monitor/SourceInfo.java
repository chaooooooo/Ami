package chao.app.ami.monitor;

/**
 * Created by qinchao on 2017/9/7.
 */

public class SourceInfo {


    private String mClassName;
    private String mMethodName;
    private String mFileName;
    private int mLineNumber;
    private StackTraceElement[] mStacks;

    public SourceInfo() {
        mClassName = "<init>";
        mMethodName = "<init>";
        mLineNumber = -1;
        mFileName = "<init>";
    }

    public SourceInfo(StackTraceElement[] stackElements, StackTraceElement element) {
        mStacks = stackElements;
        mClassName = element.getClassName();
        mMethodName = element.getMethodName();
        mFileName = element.getFileName();
        mLineNumber = element.getLineNumber();
    }

    public String getClassName() {
        return mClassName;
    }

    public String getSimpleName() {
        try {
            Class clazz = Class.forName(mClassName);
            return clazz.getSimpleName();
        } catch (ClassNotFoundException e) {
            return mClassName;
        }
    }

    public String getMethodName() {
        return mMethodName;
    }

    public String getFileName() {
        return mFileName;
    }

    public int getLineNumber() {
        return mLineNumber;
    }

    public boolean isNativeMethod() {
        return mLineNumber == -2;
    }

    public StackTraceElement[] getStackTrackElements() {
        if (mStacks == null) {
            mStacks = new StackTraceElement[0];
        }
        return mStacks;
    }

    @Override
    public String toString() {
        return getClassName() + "." + mMethodName +
                (isNativeMethod() ? "(Native Method)" :
                        (mFileName != null && mLineNumber >= 0 ?
                                "(" + mFileName + ":" + mLineNumber + ")" :
                                (mFileName != null ? "(" + mFileName + ")" : "(Unknown Source)")));
    }
}
