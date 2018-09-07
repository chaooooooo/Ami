package chao.app.ami.plugin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import chao.app.ami.Ami;
import java.util.ArrayList;
import java.util.HashMap;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class AmiPluginManager {

    private MagicIndicator mMagicIndicator;

    private ViewPager mViewPager;

    private ArrayList<AmiPlugin> mPlugins = new ArrayList<>();

    private HashMap<Class, AmiPlugin> mPluginMap = new HashMap<>();

    private PageAdapter mPageAdapter;

    private TabAdapter mTabAdapter;

    private FragmentLifecycle mLifecycle;

    private CommonNavigator commonNavigator;

    private static AmiPluginManager sInstance;

    public static AmiPluginManager newInstance(MagicIndicator tabLayout, ViewPager viewPager) {
        if (sInstance == null) {
            sInstance = new AmiPluginManager(tabLayout, viewPager);
        }
        return sInstance;
    }

    public static AmiPluginManager getInstance() {
        return sInstance;
    }

    private AmiPluginManager(MagicIndicator tabLayout, ViewPager viewPager) {
        mMagicIndicator = tabLayout;
        mViewPager = viewPager;

        mTabAdapter = new TabAdapter();
        commonNavigator = new CommonNavigator(Ami.getApp());
        commonNavigator.setAdapter(mTabAdapter);
        mMagicIndicator.setNavigator(commonNavigator);
        mLifecycle = new FragmentLifecycle();
    }


    public void addPlugin(AmiPlugin... plugins) {
        for (AmiPlugin plugin: plugins) {
            mPlugins.add(plugin);
            mPluginMap.put(plugin.getClass(), plugin);
        }
        if (mTabAdapter != null) {
            mTabAdapter.notifyDataSetChanged();
        }
        if (mPageAdapter != null) {
            mPageAdapter.notifyDataSetChanged();
        }
    }

    public AmiPlugin getPlugin(Class plugin) {
        return mPluginMap.get(plugin);
    }


    private FragmentActivity mActivity;

    private boolean mReSetup = true;

    /**
     * 只可以是FragmentActivity, 因为：
     * 1. 只有FragmentActivity有Lifecycle回调
     * 2. android.support.v4.view.ViewPager只支持android.support.v4.Fragment
     * @param activity fragmentActivity
     */
    public void setupPluginTabs(Activity activity, TextView tipView) {
        if (mActivity != null) {
            FragmentManager oldFm = mActivity.getSupportFragmentManager();
            oldFm.unregisterFragmentLifecycleCallbacks(mLifecycle);
            FragmentTransaction transaction = oldFm.beginTransaction();
            for (AmiPlugin plugin: mPlugins) {
                Fragment fragment = plugin.getFragment();
                transaction.remove(fragment);
            }
            transaction.commitAllowingStateLoss();
        }
        if (!(activity instanceof FragmentActivity)) {
            tipView.setVisibility(View.VISIBLE);
            return;
        }
        tipView.setVisibility(View.GONE);
        FragmentActivity fragmentActivity = (FragmentActivity) activity;
        mReSetup = true;
        mActivity = fragmentActivity;
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        fm.registerFragmentLifecycleCallbacks(mLifecycle, false);
        mViewPager.clearOnPageChangeListeners();
        mPageAdapter = new PageAdapter(fm);
        mViewPager.setAdapter(mPageAdapter);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    private class TabAdapter extends CommonNavigatorAdapter {

        @Override
        public int getCount() {
            return mPlugins.size();
        }

        @Override
        public IPagerTitleView getTitleView(final Context context, final int position) {
            ColorTransitionPagerTitleView titleView = new ColorTransitionPagerTitleView(context);
            AmiPlugin plugin = mPlugins.get(position);
            titleView.setText(plugin.getTitle());
            titleView.setNormalColor(Color.parseColor("#aaaaaa"));
            titleView.setSelectedColor(Color.WHITE);
            titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(position);
                }
            });
            return titleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
            indicator.setColors(Color.WHITE);
            return indicator;
        }
    }

    private class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            AmiPlugin plugin = mPlugins.get(position);
            Fragment fragment;
            if (mReSetup) {
                fragment = plugin.newFragment();
            } else {
                fragment = plugin.getFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mPlugins.size();
        }
    }

    private class FragmentLifecycle extends FragmentManager.FragmentLifecycleCallbacks {
        @Override
        public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
            super.onFragmentCreated(fm, f, savedInstanceState);
            for (AmiPlugin plugin: mPlugins) {
                if (f == plugin.getFragment()) {
                    plugin.onCreate();
                    return;
                }
            }
        }

        @Override
        public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
            super.onFragmentDestroyed(fm, f);
            for (AmiPlugin plugin: mPlugins) {
                if (f == plugin.getFragment()) {
                    plugin.onDestroy();
                    return;
                }
            }
        }
    }
}
