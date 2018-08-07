package chao.app.ami.frames;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import chao.app.ami.Ami;
import chao.app.ami.Constants;
import chao.app.ami.hooks.FragmentManagerHook;
import chao.app.ami.hooks.SupportFragmentManagerHook;

/**
 * @author chao.qin
 * @since 2017/8/13
 */

public class FirstFrame extends FrameImpl{

    private Activity activity;

    public FirstFrame(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getSize() {
        return getEntries().size();
    }

    @Override
    public Entry getEntry(int position) {
        return getEntries().get(position);
    }

    private ArrayList<Entry> getEntries() {
        ArrayList<Entry> entries = new ArrayList<>();
        //application entry
        entries.add(new Entry(Application.class.getName(), Constants.CATEGORY,null));
        entries.add(new Entry(String.valueOf(Ami.getApp()), "", Ami.getApp()));
        //activity entry
        entries.add(new Entry(Activity.class.getName(), Constants.CATEGORY,null));
        entries.add(new Entry(String.valueOf(activity), "", activity));
        //fragments
        entries.add(new Entry(Fragment.class.getName(), Constants.CATEGORY, null));
        List<Fragment> list = FragmentManagerHook.getActiveFragments(activity);
        if (list != null) {
            for (Fragment fragment: list) {
                entries.add(new Entry(String.valueOf(fragment), "", fragment));
            }
        }
        //support fragments
        entries.add(new Entry(android.support.v4.app.Fragment.class.getName(), Constants.CATEGORY, null));
        if (activity instanceof FragmentActivity) {
            ArrayList<android.support.v4.app.Fragment> fragmentList = SupportFragmentManagerHook.getActiveFragments((FragmentActivity) activity);
            if (fragmentList != null) {
                for (android.support.v4.app.Fragment fragment: fragmentList) {
                    entries.add(new Entry(String.valueOf(fragment), "", fragment));
                }
            }
        }
        //View
        entries.add(new Entry(View.class.getName(), Constants.CATEGORY, null));
        View decorView = activity.getWindow().getDecorView();
        entries.add(new Entry(String.valueOf(decorView), "", decorView));
        return entries;
    }

    @Override
    public String getName() {
        return activity.getClass().getSimpleName();
    }
}
