package chao.app.ami.classes;


import android.support.v4.util.ArrayMap;

import java.lang.reflect.Field;
import java.util.Stack;

/**
 * @author chao.qin
 * @since 2017/8/2
 */

public class FrameProcessor {

    private Stack mFrameStack = new Stack();
    private ArrayMap<String, String> mFieldMap = new ArrayMap<>();


    public void process(Object object) {
        mFrameStack.push(object);
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

    }


}
