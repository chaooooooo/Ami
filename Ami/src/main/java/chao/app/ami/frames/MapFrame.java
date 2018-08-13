package chao.app.ami.frames;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Set;

/**
 * @author chao.qin
 * @since 2017/8/15
 */

public class MapFrame extends FrameImpl {

    private String mTitle;
    private Map mMap;

    public MapFrame(String title, Map map) {
        super(map);
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
            return new Entry(mTitle, true);
        }
        int offset = position - 1;
        Set mapEntry = mMap.entrySet();
        Object array = mapEntry.toArray();
        return new Entry(Array.get(array, offset), getName(), String.valueOf(Array.get(array, offset)));
    }

    @Override
    public String getName() {
        return mTitle;
    }



}
