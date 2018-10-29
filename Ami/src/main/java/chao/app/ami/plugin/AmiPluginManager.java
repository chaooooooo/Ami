package chao.app.ami.plugin;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.plugin.plugins.general.GeneralPlugin;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class AmiPluginManager implements ViewPager.OnPageChangeListener {

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private ArrayList<AmiPlugin> mPlugins = new ArrayList<>();

    private static HashMap<Class, AmiPlugin> mPluginMap = new HashMap<>();

    private PageAdapter mPageAdapter;

    private AmiContentView contentView;

    private static AmiPluginManager sInstance;

    private GeneralPlugin mGeneralPlugin;

    private FragmentActivity mActivity;

    private int curIndicator;

    private ArrayList<AmiPlugin> mFragmentPlugins = new ArrayList<>();


    public static AmiPluginManager getInstance() {
        if (sInstance == null) {
            sInstance = new AmiPluginManager();
        }
        return sInstance;
    }

    public void initView(AmiContentView content, TabLayout tabLayout, ViewPager viewPager) {
        contentView = content;
        mTabLayout = tabLayout;
        mViewPager = viewPager;

        for (AmiPlugin plugin : mPlugins) {
            plugin.onBindView(contentView);
        }
    }

    private AmiPluginManager() {
    }

    public void addGeneralPlugin(GeneralPlugin generalPlugin) {
        mGeneralPlugin = generalPlugin;
        addPlugin(mGeneralPlugin);
    }


    @SuppressWarnings("unused")
    public void addPlugin(AmiPlugin plugin, int index) {
        mPluginMap.put(plugin.getClass(), plugin);
        mPlugins.add(index, plugin);
        plugin.onCreate();
        if (contentView != null) {
            plugin.onBindView(contentView);
        }
        if (plugin.newFragment() != null) {
            mFragmentPlugins.add(plugin);
        }
        if (mPageAdapter != null) {
            mPageAdapter.notifyDataSetChanged();
        }
    }

    public void addPlugin(AmiPlugin... plugins) {
        for (AmiPlugin plugin : plugins) {
            mPlugins.add(plugin);
            mPluginMap.put(plugin.getClass(), plugin);
            plugin.onCreate();
            if (contentView != null) {
                plugin.onBindView(contentView);
            }
            if (plugin.newFragment() != null) {
                mFragmentPlugins.add(plugin);
            }
        }
        if (mPageAdapter != null) {
            mPageAdapter.notifyDataSetChanged();
        }
    }

    @SuppressWarnings("unchecked")
    public static <Plugin extends AmiPlugin> Plugin getPlugin(Class plugin) {
        return (Plugin) mPluginMap.get(plugin);
    }

    public ArrayList<AmiPlugin> getPlugins() {
        return mPlugins;
    }

    /**
     * 只可以是FragmentActivity, 因为：
     * 1. FragmentActivity兼容性更强
     * 2. 只有FragmentActivity有Lifecycle回调
     * 3. android.support.v4.view.ViewPager只支持android.support.v4.Fragment
     *
     * @param activity fragmentActivity
     */
    public void setupPluginTabs(Activity activity, TextView tipView) {
        if (mActivity != null) {
            FragmentManager oldFm = mActivity.getSupportFragmentManager();
            FragmentTransaction transaction = oldFm.beginTransaction();
            for (AmiPlugin plugin : mFragmentPlugins) {
                Fragment fragment = plugin.getFragment();
                if (fragment == null) {
                    continue;
                }
                transaction.remove(fragment);
                plugin.destroyFragment();
            }
            transaction.commitAllowingStateLoss();
        }
        if (!(activity instanceof FragmentActivity)) {
            tipView.setVisibility(View.VISIBLE);
            return;
        }
        tipView.setVisibility(View.GONE);
        mActivity = (FragmentActivity) activity;
        FragmentManager fm = mActivity.getSupportFragmentManager();
        mPageAdapter = new PageAdapter(fm);
        mViewPager.setAdapter(mPageAdapter);

        int position = 0;
        for (AmiPlugin plugin : mPlugins) {
            plugin.onActivityChanged(mActivity);
            plugin.mFragment = fm.findFragmentByTag(mPageAdapter.makeFragmentName(position));
            position++;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (mActivity == null) {
            return false;
        }
        int cur = mViewPager.getCurrentItem();
        String tag = mPageAdapter.makeFragmentName(cur);
        FragmentManager fm = mActivity.getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag(tag);
        if (f instanceof AmiPluginFragment) {
            AmiPluginFragment pluginFragment = (AmiPluginFragment) f;
            return pluginFragment.onKeyEvent(keyEvent);
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        curIndicator = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class PageAdapter extends FragmentPagerAdapter {

        PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            IPlugin plugin = mFragmentPlugins.get(position);
            Fragment fragment = plugin.getFragment();
            if (fragment == null) {
                fragment = plugin.newFragment();
            }
            return fragment;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentPlugins.get(position).getTitle();
        }

        public String makeFragmentName(int position) {
            int viewId = mViewPager.getId();
            long id = getItemId(position);
            return "android:switcher:" + viewId + ":" + id;
        }

        @Override
        public int getCount() {
            return mFragmentPlugins.size();
        }
    }


    public GeneralPlugin getGeneralPlugin() {
        return mGeneralPlugin;
    }
}
