package chao.app.ami.plugin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import chao.app.ami.Ami;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.plugin.plugins.general.GeneralPlugin;
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

    private CommonNavigator commonNavigator;

    private AmiContentView contentView;

    private static AmiPluginManager sInstance;

    private GeneralPlugin mGeneralPlugin;

    private FragmentActivity mActivity;

    private boolean mReSetup = true;

    ArrayList<AmiPlugin> mFragmentPlugins = new ArrayList<>();


    public static AmiPluginManager getInstance() {
        if (sInstance == null) {
            sInstance = new AmiPluginManager();
        }
        return sInstance;
    }

    public void initView(AmiContentView content, MagicIndicator tabLayout, ViewPager viewPager) {
        contentView = content;
        mMagicIndicator = tabLayout;
        mViewPager = viewPager;

        mTabAdapter = new TabAdapter();
        commonNavigator = new CommonNavigator(Ami.getApp());
        commonNavigator.setAdapter(mTabAdapter);
        mMagicIndicator.setNavigator(commonNavigator);

        for (AmiPlugin plugin: mPlugins) {
            plugin.onBindView(contentView);
        }
    }

    private AmiPluginManager() {
    }

    public void addGeneralPlugin(GeneralPlugin generalPlugin) {
        mGeneralPlugin = generalPlugin;
        addPlugin(mGeneralPlugin);
    }

    public void addPlugin(AmiPlugin... plugins) {
        for (AmiPlugin plugin: plugins) {
            mPlugins.add(plugin);
            mPluginMap.put(plugin.getClass(), plugin);
            plugin.onCreate();
            if (contentView != null) {
                plugin.onBindView(contentView);
            }
            if (plugin.getFragment() != null) {
                mFragmentPlugins.add(plugin);
            }
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

    public ArrayList<AmiPlugin> getPlugins() {
        return mPlugins;
    }

    /**
     * 只可以是FragmentActivity, 因为：
     * 1. FragmentActivity兼容性更强
     * 2. 只有FragmentActivity有Lifecycle回调
     * 3. android.support.v4.view.ViewPager只支持android.support.v4.Fragment
     * @param activity fragmentActivity
     */
    public void setupPluginTabs(Activity activity, TextView tipView) {
        if (mActivity != null) {
            FragmentManager oldFm = mActivity.getSupportFragmentManager();
            FragmentTransaction transaction = oldFm.beginTransaction();
            for (IPlugin plugin: mFragmentPlugins) {
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
        mViewPager.clearOnPageChangeListeners();
        mPageAdapter = new PageAdapter(fm);
        mViewPager.setAdapter(mPageAdapter);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
        for (AmiPlugin plugin: mPlugins) {
            plugin.onActivityChanged(fragmentActivity);
        }
    }


    private class TabAdapter extends CommonNavigatorAdapter {


        @Override
        public int getCount() {
            return mFragmentPlugins.size();
        }

        @Override
        public IPagerTitleView getTitleView(final Context context, final int position) {
            ColorTransitionPagerTitleView titleView = new ColorTransitionPagerTitleView(context);
            IPlugin plugin = mFragmentPlugins.get(position);
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
            IPlugin plugin = mFragmentPlugins.get(position);
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
            return mFragmentPlugins.size();
        }
    }

    public GeneralPlugin getGeneralPlugin() {
        return mGeneralPlugin;
    }
}
