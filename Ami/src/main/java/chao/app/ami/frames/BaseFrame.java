package chao.app.ami.frames;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import chao.app.ami.AmiException;
import chao.app.ami.Constants;
import chao.app.ami.hooks.FragmentManagerHook;
import chao.app.ami.hooks.SupportFragmentManagerHook;

/**
 * @author chao.qin
 * @since 2017/8/13
 */

public class BaseFrame implements IFrame{
    Activity activity;

    public BaseFrame(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getSize() {
        List list = FragmentManagerHook.getActiveFragments(activity);
        List supportList = null;
        if (activity instanceof FragmentActivity) {
            supportList = SupportFragmentManagerHook.getActiveFragments((FragmentActivity) activity);
        }
        int size = 1;

        if (list != null) {
            size = list.size() + size;
        }
        if (supportList != null) {
            size = supportList.size() + size;
        }
        return size + 3; //Activity_Category + Fragment_Category + SupportFragment_Category
    }

    @Override
    public Entry getEntry(int position) {
        if (position == 0) {
            return new Entry(Activity.class.getName(), Constants.CATEGORY,null);
        } else if (position == 1) {
            return new Entry(String.valueOf(activity), "", activity);
        } else if (position == 2) {
            return new Entry(Fragment.class.getName(), Constants.CATEGORY, null);
        }
        //android.app.Fragment
        int offset = position - 3;
        List<Fragment> list = FragmentManagerHook.getActiveFragments(activity);
        int listSize = 0;
        if (list != null) {
            listSize = list.size();
        }
        if (list!= null && offset - listSize < 0) {
            Fragment fragment = list.get(offset);
            return new Entry(String.valueOf(fragment), "", fragment);
        }

        //android.support.v4.app.Fragment
        offset = position - listSize - 3;
        if (offset == 0) {
            return new Entry(android.support.v4.app.Fragment.class.getName(), Constants.CATEGORY, null);
        }
        if (offset < 0) {
            throw new AmiException("illegal position.");
        }
        offset = position - listSize - 4;
        List<android.support.v4.app.Fragment> fragmentList = null;
        if (activity instanceof FragmentActivity) {
            fragmentList = SupportFragmentManagerHook.getActiveFragments((FragmentActivity) activity);
        }
        if (fragmentList == null) {
            return null;
        }
        android.support.v4.app.Fragment sFragment = fragmentList.get(offset);
        return new Entry(String.valueOf(sFragment), "", sFragment);
    }

    @Override
    public String getName() {
        return activity.getClass().getSimpleName();
    }
}
