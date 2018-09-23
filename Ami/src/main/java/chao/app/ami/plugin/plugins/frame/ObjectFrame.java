package chao.app.ami.plugin.plugins.frame;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import chao.app.ami.Constants;

/**
 * @author chao.qin
 * @since 2017/8/2
 */

public class ObjectFrame extends FrameImpl implements Constants {

    private int mSize = 0;

    private Object mObject;

    private Field[][] mFields = new Field[20][];

    private Object[][] mObjects = new Object[20][];


    private Object[] mFinalFields;
    private Object[] mFinalObjects;


    private int mDeep = 0;

    private ArrayList<String> mSuperClassList = new ArrayList<>();


    public ObjectFrame(Object object) {
        super(object);
        mObject = object;
        Class<?> clazz = object.getClass();
        extractFields(clazz);

        
        int count = 0;
        for (int i = 0; i < mDeep; i++) {
            count += mFields[i].length;
        }
        mSize = count + mDeep;

        mFinalFields = new Object[mSize];
        mFinalObjects = new Object[mSize];
        int index = 0;
        for (int i = 0; i < mDeep; i++) {
            mFinalFields[index] = mSuperClassList.get(i);
            mFinalObjects[index] = Constants.CATEGORY;
            index += 1;
            if (mFields[i].length == 0) {
                continue;
            }
            System.arraycopy(mFields[i], 0, mFinalFields, index, mFields[i].length);
            System.arraycopy(mObjects[i], 0, mFinalObjects, index, mObjects[i].length);
            index += mFields[i].length;
        }
    }


    private void extractFields(Class clazz) {
        mSuperClassList.add(clazz.getName());
        Field[] fields = clazz.getDeclaredFields();
        int length = 0;
        for (Field field: fields) {
            if ((field.getModifiers() & Modifier.FINAL) != 0 && (field.getModifiers() & Modifier.STATIC) != 0) {
                continue;
            }
            length++;
        }
        mFields[mDeep] = new Field[length];
        mObjects[mDeep] = new Object[length];
        int j = 0;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            //剔除常量
            if ((field.getModifiers() & Modifier.FINAL) != 0 && (field.getModifiers() & Modifier.STATIC) != 0) {
                continue;
            }
            field.setAccessible(true);
            try {
                mFields[mDeep][j] = field;
                mObjects[mDeep][j] = field.get(mObject);
            } catch (IllegalAccessException e) {
                mObjects[mDeep][j] = OBJECT_UNKNOWN;
            }
            if (mObjects[mDeep][j] == null) {
                mObjects[mDeep][j] = OBJECT_NULL;
            }
            j++;
        }
        mDeep++;
        Class superClazz = clazz.getSuperclass();
        if (superClazz == null) {
            return;
        }
        extractFields(superClazz);
    }

    @Override
    public String getName() {
        return mObject.getClass().getSimpleName();
    }

    public String getSimpleName() {
        return mObject.getClass().getSimpleName();
    }

    @Override
    public Entry getEntry(int position) {
        if (position >= mFinalFields.length) {
            return null;
        }
        if (mFinalFields[position] instanceof Field) {
            Field field = (Field) mFinalFields[position];
            return new Entry(mFinalObjects[position], field.getName(), field.getType().getName());
        }
        return new Entry(mFinalFields[position].toString(), true);
    }

    public int getDeep() {
        return mDeep;
    }

    @Override
    public int getSize() {
        return mSize;
    }
}
