package chao.app.debug.launcher.drawer;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import java.io.Serializable;

public class DrawerNode {

    private static final String TAG = DrawerNode.class.getSimpleName();

    private static final String XML_EXTRA_FORMAT_INT = "int";
    private static final String XML_EXTRA_FORMAT_LONG = "long";
    private static final String XML_EXTRA_FORMAT_DOUBLE = "double";
    private static final String XML_EXTRA_FORMAT_FLOAT = "float";
    private static final String XML_EXTRA_FORMAT_STRING = "string";
    private static final String XML_EXTRA_FORMAT_OBJECT = "object";  //serialize or parcelable object， inner class is not supported


    private DrawerGroup mParent;
    private String mNodeName;
    private String mComponent;

    private int mFlags;   // Intent.mFlags
    
    private final static ArrayMap<String, Integer> FLAG_CONSTANT_MAP = new ArrayMap<>();
    static {
        FLAG_CONSTANT_MAP.put("FLAG_ACTIVITY_BROUGHT_TO_FRONT" ,0x00400000);
        FLAG_CONSTANT_MAP.put("FLAG_ACTIVITY_RESET_TASK_IF_NEEDED" ,0x00200000);
        FLAG_CONSTANT_MAP.put("FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY" ,0x00100000);
        FLAG_CONSTANT_MAP.put("FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET" ,0x00080000);
        FLAG_CONSTANT_MAP.put("FLAG_ACTIVITY_NEW_DOCUMENT" ,0x00080000);
        FLAG_CONSTANT_MAP.put("FLAG_ACTIVITY_NO_USER_ACTION" ,0x00040000);
        FLAG_CONSTANT_MAP.put("FLAG_ACTIVITY_REORDER_TO_FRONT" ,0X00020000);
        FLAG_CONSTANT_MAP.put("FLAG_ACTIVITY_NO_ANIMATION" ,0X00010000);
        FLAG_CONSTANT_MAP.put("FLAG_ACTIVITY_CLEAR_TASK" ,0X00008000);
        FLAG_CONSTANT_MAP.put("FLAG_ACTIVITY_TASK_ON_HOME" ,0X00004000);
        FLAG_CONSTANT_MAP.put("FLAG_ACTIVITY_RETAIN_IN_RECENTS" ,0x00002000);
        FLAG_CONSTANT_MAP.put("FLAG_ACTIVITY_LAUNCH_ADJACENT" ,0x00001000);
    }
 

    private Bundle mBundle = new Bundle();

    public void setParent(DrawerGroup node) {
        mParent = node;
        mParent.addNode(this);
    }

    public DrawerGroup getParent() {
        return mParent;
    }

    public void setNodeName(String name) {
        mNodeName = name;
    }

    public String getNodeName() {
        return mNodeName;
    }

    public void setComponent(String component) {
        mComponent = component;
    }

    public String getComponent() {
        return mComponent;
    }

    public void addExtra(String key, String extraValue, String format) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(extraValue)) {
            return;
        }
        Object value = extraValue;
        try {
            switch (format) {
                case XML_EXTRA_FORMAT_INT:
                    value = Integer.valueOf(extraValue);
                    mBundle.putInt(key, (int) value);
                    break;
                case XML_EXTRA_FORMAT_LONG:
                    value = Long.valueOf(extraValue);
                    mBundle.putLong(key, (Long) value);
                    break;
                case XML_EXTRA_FORMAT_DOUBLE:
                    value = Double.valueOf(extraValue);
                    mBundle.putDouble(key, (Double) value);
                    break;
                case XML_EXTRA_FORMAT_FLOAT:
                    value = Float.valueOf(extraValue);
                    mBundle.putFloat(key, (Float) value);
                    break;
                case XML_EXTRA_FORMAT_STRING:
                    mBundle.putString(key, (String) value);
                    break;
                case XML_EXTRA_FORMAT_OBJECT:
                    Class<?> clazz;
                    Object o;
                    try {
                        clazz = Class.forName(extraValue);
                        o = clazz.newInstance(); //不支持内部类
                    } catch (ClassNotFoundException e) {
                        throw new DrawerParserException("Cannot found class for class name: " + extraValue, e);
                    } catch (InstantiationException e) {
                        throw new DrawerParserException("Cannot instantiation class for class name :" + extraValue, e);
                    } catch (IllegalAccessException e) {
                        throw new DrawerParserException("Cannot access class for class name : " + extraValue, e);
                    }
                    if (o instanceof Serializable) {
                        mBundle.putSerializable(key, (Serializable) o);
                    } else if (o instanceof Parcelable) {
                        mBundle.putParcelable(key, (Parcelable) o);
                    }
                    break;
            }
        } catch (NumberFormatException e) {
            mBundle.putString(key, extraValue);
        }
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public void setFlags(String aflags) {
        if (TextUtils.isEmpty(aflags)) {
            return;
        }
        String[] flagArray = aflags.split("\\|");
        for (String flag : flagArray) {
            flag = flag.trim();
            if (TextUtils.isEmpty(flag)) {
                continue;
            }
            Integer integer = FLAG_CONSTANT_MAP.get(flag);
            if (integer != null && integer != 0) {
                mFlags |= integer;
                continue;
            }
            int flagValue;
            try {
                flagValue = Integer.parseInt(flag);
            } catch (NumberFormatException e) {
                try {
                    flag = flag.replaceAll("0x","");
                    flagValue = Integer.parseInt(flag, 16);
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "value cannot be recognized : " + flag);
                    flagValue = 0;
                }
            }
            mFlags |= flagValue;
        }
    }

    public int getFlags() {
        return mFlags;
    }
}
