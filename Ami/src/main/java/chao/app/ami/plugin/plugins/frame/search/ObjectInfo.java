package chao.app.ami.plugin.plugins.frame.search;


import android.text.SpannableString;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author chao.qin
 * @since 2018/8/8
 */

public class ObjectInfo implements Serializable {

    private static final SpannableString EMPTY_SPANNABLE_STRING = SearchManager.EMPTY_SPANNABLE_STRING;
    /**
     * 类名
     */
    private SpannableString className;


    /**
     *
     */
    private ObjectInfo parent;

    /**
     *  名称
     */
    private SpannableString name;

    /**
     * 值， 字符串化
     */
    private SpannableString value;


    private Field field;

    private Object object;

    private int deep;

    public ObjectInfo(Object object) {
        this.object = object;
        this.field = null;
        this.value = new SpannableString(String.valueOf(object));
        this.name = EMPTY_SPANNABLE_STRING;
        this.className = new SpannableString(object.getClass().getName());
        this.parent = null;
        this.deep = 0;
    }

    ObjectInfo(Field field, Object object, ObjectInfo parent) {
        this.object = object;
        this.field = field;
        this.value = new SpannableString(String.valueOf(object));
        this.name = new SpannableString(field.getName());
        this.className = new SpannableString(field.getType().getName());
        this.parent = parent;
        this.deep = parent.getDeep() + 1;
    }

    public SpannableString getClassName() {
        return className;
    }

    public void setClassName(SpannableString className) {
        this.className = className;
    }

    public ObjectInfo getParent() {
        return parent;
    }

    public void setParent(ObjectInfo parent) {
        this.parent = parent;
    }

    public SpannableString getName() {
        return name;
    }

    public void setName(SpannableString name) {
        this.name = name;
    }

    public SpannableString getValue() {
        return value;
    }

    public void setValue(SpannableString value) {
        this.value = value;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    @Override
    public String toString() {
        return "ObjectInfo{" +
                "parent=" + String.valueOf(parent.getClassName()) +
                ",className=" + className +
                ", name=" + name +
                ", value=" + value +
//                ", object=" + object +
                ", deep=" + deep +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        ObjectInfo that = (ObjectInfo) o;

        if (object != null && object == that.object) {
            return true;
        }

        if (object != null) {
            return false;
        }
        //object == null
        Object thisParent = findNotNullParent(this);
        Object thatParent = findNotNullParent(that);

        if (thisParent == thatParent) {
            return true;
        }
        return false;

    }

    private Object findNotNullParent(ObjectInfo parentInfo) {
        while (parentInfo != null) {
            Object parent = parentInfo.getObject();
            if (parent != null) {
                return parent;
            }
            parentInfo = parentInfo.getParent();
        }
        return null;
    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (field != null ? field.hashCode() : 0);
        return result;
    }
}
