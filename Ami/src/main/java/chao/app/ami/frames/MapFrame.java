package chao.app.ami.frames;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Set;

import chao.app.ami.Constants;

/**
 * @author chao.qin
 * @since 2017/8/15
 */

public class MapFrame extends FrameImpl {

    private String mTitle;
    private Map mMap;

    public MapFrame(String title, Map map) {
        mTitle = title;
        mMap = map;
    }

    @Override
    public int getSize() {
        return mMap.size() + 1;
    }

    @Override
    public Entry getEntry(int position) {
        if (position == 0) {
            return new Entry(mTitle, Constants.CATEGORY, null);
        }
        int offset = position - 1;
        Set<Map.Entry> mapEntry = mMap.entrySet();
        Object array = mapEntry.toArray();
        return new Entry(String.valueOf(Array.get(array, offset)), "", Array.get(array, offset));
    }

    @Override
    public String getName() {
        return mTitle;
    }



}
