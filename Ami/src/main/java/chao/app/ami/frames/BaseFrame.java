package chao.app.ami.frames;

import android.app.Activity;
import android.app.Fragment;

import java.util.List;

import chao.app.ami.hooks.FragmentManagerHook;

/**
 * @author chao.qin
 * @since 2017/8/13
 */

public class BaseFrame implements IFrame{
    Activity activity;
    Fragment fragment;
    android.support.v4.app.Fragment supportFragment;

    public BaseFrame(Activity activity) {
        this.activity = activity;

    }

    @Override
    public int getSize() {
        List list = FragmentManagerHook.getActiveFragments(activity);
        if (list != null) {
            return list.size() + 1;
        }
        return 1;
    }

    @Override
    public Entry getEntry(int position) {
        if (position == 0) {
            return new Entry(activity.getClass().getName(), activity.getClass().getName(), activity);
        }
        int index = position - 1;
        List<Fragment> list = FragmentManagerHook.getActiveFragments(activity);
        if (list == null) {
            return null;
        }
        Fragment fragment = list.get(index);
        return new Entry(fragment.getClass().getName(), fragment.getClass().getName(), fragment);
    }

    @Override
    public String getName() {
        return activity.getClass().getSimpleName();
    }
}
