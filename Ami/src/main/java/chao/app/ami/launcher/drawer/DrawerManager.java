package chao.app.ami.launcher.drawer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import chao.app.ami.ActivitiesLifeCycleAdapter;
import chao.app.ami.Ami;
import chao.app.ami.R;
import chao.app.ami.UI;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.hooks.FragmentLifecycle;
import chao.app.ami.hooks.WindowCallbackHook;
import chao.app.ami.launcher.drawer.node.ComponentNode;
import chao.app.ami.launcher.drawer.node.DrawerNode;
import chao.app.ami.launcher.drawer.node.Node;
import chao.app.ami.launcher.drawer.node.NodeGroup;
import chao.app.ami.plugin.AmiPluginManager;
import chao.app.ami.plugin.plugins.colorful.ColorfulPlugin;
import chao.app.ami.plugin.plugins.fps.FPSPlugin;
import chao.app.ami.plugin.plugins.frame.FramePlugin;
import chao.app.ami.plugin.plugins.general.GeneralPlugin;
import chao.app.ami.plugin.plugins.info.InfoPlugin;
import chao.app.ami.plugin.plugins.logcat.LogcatPlugin;
import chao.app.ami.plugin.plugins.store.StorePlugin;
import chao.app.ami.plugin.plugins.viewinterceptor.InterceptorLayerManager;
import chao.app.ami.plugin.plugins.viewinterceptor.ViewInterceptorPlugin;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;


public class DrawerManager implements DrawerXmlParser.DrawerXmlParserListener, View.OnClickListener, WindowCallbackHook.DispatchKeyEventListener, ViewGroup.OnHierarchyChangeListener {

    @SuppressLint("StaticFieldLeak")
    private static DrawerManager sDrawerManager;

    private RecyclerView mDrawerListView;
    private DrawerAdapter mDrawerAdapter;

    private DrawerNode mDrawerRootNode;

    private DrawerLayout mDrawerLayout;

    private ImageView mNavigationBackView;
    private TextView mNavigationPathView;

    private int mDrawerId;

    private InterceptorLayerManager mInterceptorManager;
    private ViewGroup mRealView;
    private ViewGroup mDecorView;


    private Context mContext = Ami.getApp();

    private Activity mActivity;

    private AmiPluginManager mPluginManager;

    private DrawerManager() {
    }

    public void setupView(Activity activity) {
        if (mActivity != null && mActivity == activity) {
            return;
        }
        mActivity = activity;
        ViewGroup decorView = activity.findViewById(android.R.id.content);
        int decorViewChildCount = decorView.getChildCount();
        if (decorViewChildCount == 0) {
            return;
        }
        if (mDecorView == decorView) {
            return;
        }
        FrameLayout realView = new FrameLayout(activity);
        realView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View[] decorChildren = new View[decorViewChildCount];
        for (int i=0; i<decorViewChildCount; i++) {
            decorChildren[i] = decorView.getChildAt(i);
        }
        decorView.removeAllViews();
        for (int i=0;i<decorViewChildCount;i++) {
            View child = decorChildren[i];
            if (child == null) {
                continue;
            }
            realView.addView(child);
        }

        if (mDecorView != null) {
            mInterceptorManager.cleanSelected();
            mDrawerLayout.removeView(mRealView);
            mDecorView.removeAllViews();
            if (mRealView != null) {
                mDecorView.addView(mRealView);
            }
        }

        mDecorView = decorView;
        mRealView = realView;

        if (mDrawerLayout == null) {
            LayoutInflater inflater = LayoutInflater.from(Ami.getApp());
            mDrawerLayout = (DrawerLayout) inflater.inflate(R.layout.drawer_launcher, mDecorView, false);
            AmiContentView content = mDrawerLayout.findViewById(R.id.ami_content);

            mInterceptorManager = (InterceptorLayerManager) AmiPluginManager.getPlugin(ViewInterceptorPlugin.class).getManager();
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            content.addView(mInterceptorManager.getLayout(),layoutParams);

            View componentContent = findViewById(R.id.drawer_component_content);
            mNavigationBackView = componentContent.findViewById(R.id.navigation_back);
            mNavigationBackView.setOnClickListener(this);
            mNavigationPathView = componentContent.findViewById(R.id.navigation_title);
            mDrawerListView = componentContent.findViewById(R.id.ui_list);
            mDrawerListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            mDrawerListView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));

//            ViewInterceptor interceptor = new ViewInterceptor();
//            mInterceptorFrame.setInterceptor(interceptor);

            ViewPager viewPager = findViewById(R.id.drawer_plugins_view_pager);
            TabLayout tabLayout = findViewById(R.id.drawer_plugins_tab_layout);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setupWithViewPager(viewPager, true);

            mPluginManager.initView(content, tabLayout, viewPager);

            DrawerXmlParser parser = new DrawerXmlParser();
            if (mDrawerId != 0) {
                parser.parseDrawer(mContext.getResources().openRawResource(mDrawerId), this);
            }


            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                Ami.log("external sdcard not mounted.");
                return;
            }
            File externalDir = Environment.getExternalStorageDirectory();
            File amiXml = new File(externalDir, "ami.xml");
            if (amiXml.exists()) {
                try {
                    parser.parseDrawer(new FileInputStream(amiXml), this);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Ami.log("ami xml is not exist: " + amiXml.getParent());
            }
        }
        TextView tipView = mDrawerLayout.findViewById(R.id.ami_useless_tip_view);
        if (mPluginManager != null) {
            mPluginManager.setupPluginTabs(activity, tipView);
        }
        mDecorView.addView(mDrawerLayout);
        mDrawerLayout.addView(mRealView, 0);
    }

    private <T extends View> T findViewById(int resId) {
        return (T) mDrawerLayout.findViewById(resId);
    }

    public void setDrawerId(int rawId) {
        mDrawerId = rawId;
    }

    @Override
    public void onXmlParserDone(DrawerNode rootNode) {
        if (mDrawerRootNode == null) {
            mDrawerRootNode = rootNode;
            mDrawerAdapter = new DrawerAdapter(mDrawerRootNode);
            mDrawerListView.setAdapter(mDrawerAdapter);
        } else {
            mDrawerRootNode.addNode(rootNode);
            mDrawerAdapter.updateNavigation();
            mDrawerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onXmlParserFailed(Exception e) {

    }

    @Override
    public void onClick(View v) {
        if (v == mNavigationBackView) {
            mDrawerAdapter.navigationUp();
        }
    }

    @Override
    public boolean onDispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() != KeyEvent.KEYCODE_BACK) {
            return false;
        }
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                if (!mDrawerAdapter.onBackPressed()) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }

            if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                if (!mPluginManager.dispatchKeyEvent(keyEvent)) {
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                }
                return true;
            }
        }
        return false;
    }


    @Override
    public void onChildViewAdded(View parent, View child) {
        mInterceptorManager.injectListeners((ViewGroup) parent, child);
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        //do nothing
    }

    private class DrawerAdapter extends RecyclerView.Adapter {

        private NodeGroup mCurrentGroup;

        private DrawerAdapter(NodeGroup groupNode) {
            mCurrentGroup = groupNode;
            updateNavigation();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.simpel_drawer_item_layout, parent, false)) {};
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            View itemView = holder.itemView;
            TextView textView = itemView.findViewById(R.id.drawer_item_name);
            ImageView arrow = itemView.findViewById(R.id.drawer_item_arrow);
            Node node = mCurrentGroup.getChild(position);
            int visible = View.INVISIBLE;
            if (node instanceof NodeGroup) {
                visible = View.VISIBLE;
            }
            arrow.setVisibility(visible);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Node node = mCurrentGroup.getChild(holder.getAdapterPosition());
                    if (node instanceof NodeGroup) {
                        NodeGroup group = (NodeGroup) node;
                        mDrawerAdapter.navigationTo(group);
                    } else if (node instanceof ComponentNode){
                        ComponentNode componentNode = (ComponentNode) node;
                        try {
                            mDrawerLayout.closeDrawer(GravityCompat.START, false);
                            Class<?> clazz = componentName(componentNode);
                            UI.show(mContext, clazz, componentNode.getBundle(), componentNode.getFlags(), componentNode.getPermissionArray());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            textView.setText(mCurrentGroup.getChild(position).getName());
        }

        private Class componentName(ComponentNode node) throws ClassNotFoundException {
            String packageName = mDrawerRootNode.getPackageName();
            String component = node.getComponent();
            if (component.charAt(0) == '.') {
                component = packageName + component;
            }
            return Class.forName(component);
        }

        @Override
        public int getItemCount() {
            return mCurrentGroup.size();
        }

        public boolean navigationUp() {
            NodeGroup group = (NodeGroup) mCurrentGroup.getParent();
            if (group != null) {
                mCurrentGroup = group;
                notifyDataSetChanged();
                updateNavigation();
                return true;
            }
            return false;
        }

        public void navigationTo(NodeGroup groupNode) {
            if (groupNode == null) {
                return;
            }
            mCurrentGroup = groupNode;
            notifyDataSetChanged();
            updateNavigation();
        }

        private void updateNavigation() {
            if (mCurrentGroup.getParent() != null) {
                mNavigationBackView.setImageResource(R.drawable.ic_navigation);
            } else {
                mNavigationBackView.setImageResource(0);
            }
            mNavigationPathView.setText(buildNavigationTitle(mCurrentGroup));
        }

        private String buildNavigationTitle(Node node) {
            if (node == null) {
                return "/";
            }
            String title = node.getName();
            if (title == null) {
                title = "";
            }
            NodeGroup parent = (NodeGroup) node.getParent();
            while (parent != null) {
                String parentName = parent.getName();
                if (!TextUtils.isEmpty(parentName)) {
                    title = parent.getName() + "/" +  title;
                }
                parent = (NodeGroup) parent.getParent();
            }

            return title;
        }

        public boolean onBackPressed() {
            return navigationUp();
        }
    }

    private void initActivityListener() {

        Ami.getApp().registerActivityLifecycleCallbacks(new ActivitiesLifeCycleAdapter(){

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                //hook点击事件
                activity.getWindow().setCallback(WindowCallbackHook.newInstance(activity, sDrawerManager));

                //hook fragment
//                FragmentManagerHook.hook(activity);


                if (activity instanceof FragmentActivity) {
                    FragmentActivity fActivity = (FragmentActivity) activity;
                    FragmentManager supportManager = fActivity.getSupportFragmentManager();
                    supportManager.registerFragmentLifecycleCallbacks(new FragmentLifecycle(), true);
                }

            }

            @Override
            public void onActivityStarted(Activity activity) {
                sDrawerManager.setupView(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                sDrawerManager.injectInput(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                super.onActivityStopped(activity);
                sDrawerManager.mInterceptorManager.cleanSelected();
            }
        });
    }

    public void injectInput(Activity activity) {
        Intent intent = activity.getIntent();
        if (intent == null) {
            return;
        }
        Map<String, String> inputs = (Map<String, String>) intent.getSerializableExtra(DrawerConstants.EXTRA_KEY_INPUT);
        if (inputs == null) {
            return;
        }
        Set<String> viewIds = inputs.keySet();
        if (viewIds.size() == 0) {
            return;
        }
        for (String viewId : viewIds) {
            View view = null;
            String[] ids = viewId.split("\\.");
            if (ids.length == 0) {
                ids = new String[]{viewId};
            }
            for (String id: ids) {
                int resId = activity.getResources().getIdentifier(id, "id", Ami.getApp().getPackageName());
                if (view == null) {
                    view = activity.findViewById(resId);
                } else {
                    view = view.findViewById(resId);
                }
                if (view == null) {
                    return;
                }
                if (view instanceof TextView) {
                    ((TextView) view).setText(inputs.get(viewId));
                }
            }
        }
    }

    public View getRealView() {
        return mRealView;
    }

    public static DrawerManager get() {
        if (sDrawerManager == null) {
            sDrawerManager = new DrawerManager();
            sDrawerManager.init();
        }
        return sDrawerManager;
    }

    private void init() {
        mPluginManager = AmiPluginManager.getInstance();
        mPluginManager.addGeneralPlugin(new GeneralPlugin());
        mPluginManager.addPlugin(new LogcatPlugin(),
            new FramePlugin(),
            new InfoPlugin(),
            new ViewInterceptorPlugin(),
            new StorePlugin(),
            new ColorfulPlugin()
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mPluginManager.addPlugin(new FPSPlugin());
        }

        initActivityListener();
    }


}