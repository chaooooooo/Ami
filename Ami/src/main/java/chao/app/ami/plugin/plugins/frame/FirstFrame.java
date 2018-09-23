package chao.app.ami.plugin.plugins.frame;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import chao.app.ami.Ami;
import chao.app.ami.hooks.FragmentManagerHook;
import chao.app.ami.hooks.SupportFragmentManagerHook;

/**
 * @author chao.qin
 * @since 2017/8/13
 */

public class FirstFrame extends FrameImpl{

    private Activity activity;

    public FirstFrame(Activity activity) {
        super(activity);
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
        entries.add(new Entry(Application.class.getName(), true));
        entries.add(new Entry(Ami.getApp(), "Application", Application.class.getName()));
        //activity entry
        entries.add(new Entry(Activity.class.getName(), true));
        entries.add(new Entry(activity, "Activity", activity.getClass().getName()));
        //fragments
        entries.add(new Entry(Fragment.class.getName(), true));
        List<Fragment> list = FragmentManagerHook.getActiveFragments(activity);
        if (list != null) {
            for (Fragment fragment: list) {
                entries.add(new Entry(fragment, "", fragment.getClass().getName()));
            }
        }
        //support fragments
        entries.add(new Entry(android.support.v4.app.Fragment.class.getName(), true));
        if (activity instanceof FragmentActivity) {
            ArrayList<android.support.v4.app.Fragment> fragmentList = SupportFragmentManagerHook.getActiveFragments((FragmentActivity) activity);
            if (fragmentList != null) {
                for (android.support.v4.app.Fragment fragment: fragmentList) {
                    entries.add(new Entry(fragment, "", fragment.getClass().getName()));
                }
            }
        }
        //View
        entries.add(new Entry(View.class.getName(), true));
        View decorView = activity.getWindow().getDecorView();
        entries.add(new Entry(decorView, "mDecorView", decorView.getClass().getName()));
        return entries;
    }

    @Override
    public String getName() {
        return activity.getClass().getSimpleName();
    }
}
