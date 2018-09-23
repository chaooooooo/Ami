package chao.app.ami.plugin.plugins.frame.search;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chao.app.ami.plugin.plugins.frame.Constants;

/**
 * @author chao.qin
 * @since 2018/8/8
 */

public class ObjectProcessor implements Constants{

    private static final ForegroundColorSpan HIGH_LIGHT_COLOR = new ForegroundColorSpan(Color.RED);

    public static ArrayList<ObjectInfo> infoList = new ArrayList<>();

    private ObjectSearchListener objectSearchListener;

    private HashMap<String, Boolean> cancelTaskMap = new HashMap<>();

    public void startSearch(ArrayList<ObjectInfo> searchRst, String keyword, ObjectInfo targetInfo, String taskId) {
        cancelTaskMap.put(taskId, false);
        searchObject(searchRst, keyword, targetInfo, taskId);
    }

    public void stopSearch(String taskId) {
        cancelTaskMap.put(taskId, true);
    }

    public void clearTask(String taskId) {
        cancelTaskMap.remove(taskId);
    }

    private void searchObject(ArrayList<ObjectInfo> searchRst, String keyword, ObjectInfo targetInfo, String taskId) {
        if (keyword == null || keyword.length() < MIN_SEARCH_LENGTH) {
            return;
        }
        if (cancelTaskMap.get(taskId)) {
            return;
        }
        Object target = targetInfo.getObject();
        Field targetField = targetInfo.getField();


        int size = 0;
        int index = 0;
        Field[] allFields = new Field[size];

        Class<?> superClass;
        if (targetField != null) {
            superClass = targetField.getType();
        } else {
            superClass = target.getClass();
        }
        while (superClass != null && superClass != Object.class) {
            Field[] fields = superClass.getDeclaredFields();
            index = size;
            size =  size + fields.length;
            if (fields.length > 0) {
                Field[] oldFields = allFields;
                allFields = new Field[size];
                System.arraycopy(oldFields, 0, allFields, 0, oldFields.length);
                System.arraycopy(fields, 0, allFields, index, fields.length);
            }
            //superClass = superClass.getGenericSuperclass() todo 泛型类型解析
            superClass = superClass.getSuperclass();
        }

        ArrayList<ObjectInfo> children = new ArrayList<>();
        for (Field field: allFields) {
            //过滤常量
            if (fieldFilter(field)) {
                continue;
            }

            field.setAccessible(true);

            Object obj = null;
            if (target != null) {
                try {
                    obj = field.get(target);
                } catch (IllegalAccessException e) {
                    obj = null;
                }
            }
            ObjectInfo objectInfo = new ObjectInfo(field, obj, targetInfo);

            Class<?> type = field.getType();
            if (matches(keyword, objectInfo) && !searchRst.contains(objectInfo)) {
                searchRst.add(objectInfo);
                if (objectSearchListener != null) {
                    objectSearchListener.newValueFound(objectInfo);
                }
            }
            //过滤基本类型
            if (typeFilter(type)) {
                continue;
            }

            //不搜索数组或者集合
            if (field.getType().isArray()
                    || List.class.isAssignableFrom(type)
                    || Set.class.isAssignableFrom(type)
                    || Map.class.isAssignableFrom(type)) {
                continue;
            }

            if (objectInfo.getDeep() >= MAX_DEEP) {
                continue;
            }

            //检查是否存在循环引用的情况
            if (!checkType(field.getType(), objectInfo.getParent())) {
                continue;
            }

            children.add(objectInfo);
        }
        //优化搜索算法， 搜索深度由浅到深
        for (ObjectInfo objectInfo: children) {
            searchObject(searchRst, keyword, objectInfo, taskId);
        }

    }

    private boolean checkType(Class<?> type, ObjectInfo parent) {
        int count = 0;
        String typeName = type.getName();
        while (parent != null) {
            String parentName = parent.getClassName().toString();
            if (typeName.equals(parentName)) {
                count ++;
            }
            if (count >= MAX_SAME_COUNT) {
                return false;
            }
            parent = parent.getParent();
        }
        return true;
    }


    private boolean fieldFilter(Field field) {
        //过滤常量
        return (field.getModifiers() & Modifier.STATIC) != 0 && (field.getModifiers() & Modifier.FINAL) != 0;

    }

    private boolean typeFilter(Class<?> type) {
        //过滤基本类型
        return type == int.class || type == Integer.class
                || type == float.class || type == Float.class
                || type == double.class || type == Double.class
                || type == byte.class || type == Byte.class
                || type == short.class || type == Short.class
                || type == long.class || type == Long.class
                || type == boolean.class || type == Boolean.class
                || type == String.class
                || type == Class.class
                || type == Object.class;
    }


    private boolean matches(String keyword, ObjectInfo objectInfo) {

        if (searchWord(objectInfo.getClassName(), keyword)
                || searchWord(objectInfo.getName(), keyword)
                || searchWord(objectInfo.getValue(), keyword)) {
            return true;
        }
        return false;
    }

    public static boolean searchWord(SpannableString spanText, String keyword) {
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(spanText);
        boolean find = false;
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            spanText.setSpan(HIGH_LIGHT_COLOR, start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            find = true;
        }
        return find;
    }

    public void setObjectSearchListener(ObjectSearchListener objectSearchListener) {
        this.objectSearchListener = objectSearchListener;
    }

    public interface ObjectSearchListener {
        void newValueFound(ObjectInfo objectInfo);
    }
}
