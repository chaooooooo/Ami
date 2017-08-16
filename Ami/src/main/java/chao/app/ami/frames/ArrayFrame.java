package chao.app.ami.frames;

import java.lang.reflect.Array;

import chao.app.ami.Constants;

/**
 * @author chao.qin
 * @since 2017/8/15
 */

class ArrayFrame extends FrameImpl {

    private Object mArray;
    private String mTitle;

    public ArrayFrame(String title, Object array) {
        mArray = array;
        mTitle = title;
    }

    @Override
    public int getSize() {
        return Array.getLength(mArray) + 1;
    }

    @Override
    public Entry getEntry(int position) {
        if (position == 0) {
            return new Entry(mTitle, Constants.CATEGORY, null);
        }
        int offset = position - 1;

        return new Entry(String.valueOf(Array.get(mArray,offset)),"", Array.get(mArray, offset));
    }

    @Override
    public String getName() {
        return mTitle;
    }
}
