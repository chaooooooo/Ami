package chao.app.ami.launcher.drawer.node;

import android.text.TextUtils;
import chao.app.ami.utils.ReflectUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class ObjectExtra extends NodeGroup implements IObjectExtraParent {

    private static final String XML_EXTRA_FORMAT_BOOLEAN = "boolean";
    private static final String XML_EXTRA_FORMAT_INT = "int";
    private static final String XML_EXTRA_FORMAT_LONG = "long";
    private static final String XML_EXTRA_FORMAT_DOUBLE = "double";
    private static final String XML_EXTRA_FORMAT_FLOAT = "float";
    private static final String XML_EXTRA_FORMAT_STRING = "String";
    private static final String XML_EXTRA_FORMAT_OBJECT = "object";  //serialize or parcelable object， inner class is not supported


    private Class clazz;

    private Object instance;

    private String className;

    public ObjectExtra(String name, String className) {
        super(name);
        this.className = className;
        try {
            clazz = Class.forName(className);
            instance = clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addObjectExtra(ObjectExtra objectExtra) {
        String name = objectExtra.getName();
        Object o = objectExtra.getObject();
        if (TextUtils.isEmpty(name)) {
            return;
        }
        if (o == null) {
            return;
        }
        try {
            Class paramClazz = Class.forName(objectExtra.getClassName());
            String methodName = "set" + upFirstChar(objectExtra.getName());
            Method method = clazz.getMethod(methodName, paramClazz);
            method.invoke(instance, objectExtra.getObject());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addProperty(Property property) {
        super.addProperty(property);

        String key = property.getKey();
        String value = property.getValue();
        String format = property.getFormat();

        Field field = ReflectUtil.getField(clazz, key);
        if (field == null) {
            return;
        }
        try {
            field.set(instance, formatValue(value, format));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Object formatValue(String extraValue ,String format) {
        Object result = null;
        switch (format) {
            case XML_EXTRA_FORMAT_BOOLEAN:
                result = Boolean.valueOf(extraValue);
                break;
            case XML_EXTRA_FORMAT_INT:
                result = Integer.valueOf(extraValue);
                break;
            case XML_EXTRA_FORMAT_LONG:
                result = Long.valueOf(extraValue);
                break;
            case XML_EXTRA_FORMAT_DOUBLE:
                result = Double.valueOf(extraValue);
                break;
            case XML_EXTRA_FORMAT_FLOAT:
                result = Float.valueOf(extraValue);
                break;
                default:
                    result = extraValue;
        }
        return result;
    }

    private String upFirstChar(final String key) {
        char first = key.charAt(0);
        if (first >= 'a' && first <= 'z') {
            char upCase = (char) (first + ('A' - 'a'));
            return key.replaceFirst(String.valueOf(first), String.valueOf(upCase));
        }
        return key;
    }

    public Object getObject() {
        return instance;
    }

    public String getClassName() {
        return className;
    }
}
