package chao.app.debug.launcher.drawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import chao.app.debug.DebugTools;
import chao.app.debug.R;


public class DrawerLauncher extends AppCompatActivity implements DrawerXmlParser.DrawerXmlParserListener, View.OnClickListener {

    private RecyclerView mDrawerListView;
    private DrawerAdapter mDrawerAdapter;

    private DrawerGroup mDrawerRootNode;

    private DrawerLayout mDrawerLayout;

    private ImageView mNavigationBackView;
    private TextView mNavigationPathView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_launcher);

        mNavigationBackView = (ImageView) findViewById(R.id.navigation_back);
        mNavigationBackView.setOnClickListener(this);
        mNavigationPathView = (TextView) findViewById(R.id.navigation_title);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (RecyclerView) findViewById(R.id.ui_list);
        mDrawerListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        mDrawerListView.setAdapter(new DrawerAdapter());
        DrawerXmlParser parser = new DrawerXmlParser();
        parser.parseDrawer(getResources().openRawResource(drawerXmlId()), this);

    }

    public int drawerXmlId() {
        DrawerXmlID xmlID = getClass().getAnnotation(DrawerXmlID.class);
        if (xmlID != null) {
            return xmlID.value();
        }
        return 0;
    }

    @Override
    public void onXmlParserDone(DrawerGroup rootNode) {
        mDrawerRootNode = rootNode;
        mDrawerAdapter = new DrawerAdapter(mDrawerRootNode);
        mDrawerListView.setAdapter(mDrawerAdapter);
    }

    @Override
    public void onXmlParserFailed(Exception e) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.navigation_back) {
            mDrawerAdapter.navigationUp();

        }
    }

    private class DrawerAdapter extends RecyclerView.Adapter {

        private DrawerGroup mCurrentGroupNode;

        public DrawerAdapter(DrawerGroup groupNode) {
            mCurrentGroupNode = groupNode;
            updateNavigation();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(DrawerLauncher.this).inflate(android.R.layout.simple_list_item_1, parent, false)) {};
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            TextView textView = (TextView) holder.itemView;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DrawerNode drawerNode = mCurrentGroupNode.getChild(position);
                    if (drawerNode instanceof DrawerGroup) {
                        DrawerGroup group = (DrawerGroup) drawerNode;
                        mDrawerAdapter.navigationTo(group);
                    } else {
                        try {
                            mDrawerLayout.closeDrawers();
                            Class<?> clazz = Class.forName(drawerNode.getComponent());
                            DebugTools.show(DrawerLauncher.this, clazz);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            textView.setText(mCurrentGroupNode.getChild(position).getNodeName());
        }

        @Override
        public int getItemCount() {
            return mCurrentGroupNode.size();
        }

        private void navigationUp() {
            DrawerGroup group = mCurrentGroupNode.getParent();
            if (group != null) {
                mCurrentGroupNode = group;
                notifyDataSetChanged();
                updateNavigation();
            }
        }

        private void navigationTo(DrawerGroup groupNode) {
            if (groupNode == null) {
                return;
            }
            mCurrentGroupNode = groupNode;
            notifyDataSetChanged();
            updateNavigation();
        }

        private void updateNavigation() {
            if (mCurrentGroupNode.getParent() != null) {
                mNavigationBackView.setImageResource(R.drawable.ic_navigation);
            } else {
                mNavigationBackView.setImageResource(0);
            }
            mNavigationPathView.setText(buildNavigationTitle(mCurrentGroupNode));
        }

        private String buildNavigationTitle(DrawerNode node) {
            if (node == null) {
                return "/";
            }
            String title = node.getNodeName();
            if (title == null) {
                title = "";
            }
            DrawerNode parent = node.getParent();
            while (parent != null) {
                String parentName = parent.getNodeName();
                if (!TextUtils.isEmpty(parentName)) {
                    title = parent.getNodeName() + "/" +  title;
                }
                parent = parent.getParent();
            }

            return title;
        }
    }

}