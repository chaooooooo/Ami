package chao.app.ami.plugin.plugins.frame;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import chao.app.ami.Ami;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiPluginFragment;
import chao.app.ami.plugin.plugins.frame.search.ObjectInfo;
import chao.app.ami.plugin.plugins.frame.search.SearchManager;
import chao.app.ami.plugin.plugins.frame.search.SearchTextListener;
import chao.app.debug.R;
import java.util.ArrayList;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class FramePluginFragment extends AmiPluginFragment implements FrameManager.TopFrameChangedListener, View.OnClickListener, SearchManager.SearchListener {

    private RecyclerView mFrameListView;
    private FrameAdapter mFrameAdapter;
    private ImageView mFrameNavigationBackView;
    private TextView mFrameNavigationPathView;
    private AutoCompleteTextView mSearchBar;

    private SearchTextListener mSearchTextListener;

    private SearchManager mSearchManager;

    private View mSearchProgress;

    public FramePluginFragment() {
        mSearchManager = SearchManager.getInstance();
        mSearchManager.setSearchListener(this);
        mSearchTextListener = new SearchTextListener(mSearchManager);
    }


    @Override
    public void setupView(View layout) {
        super.setupView(layout);
        FrameManager frameManager = FrameManager.getInstance();
        frameManager.addFrameChangeListener(this);
        View frameContent = findView(R.id.drawer_frame_content);

        Context context = Ami.getApp();

        mFrameListView = (RecyclerView) frameContent.findViewById(R.id.frame_list);
        mFrameListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mFrameListView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        mFrameAdapter = new FrameAdapter(mFrameListView);
        mFrameListView.setAdapter(mFrameAdapter);
        mFrameNavigationBackView = (ImageView) frameContent.findViewById(R.id.navigation_back);
        mFrameNavigationBackView.setOnClickListener(this);
        mFrameNavigationPathView = (TextView) frameContent.findViewById(R.id.navigation_title);

        mSearchBar = (AutoCompleteTextView) frameContent.findViewById(R.id.search_bar);
        //处理搜索
        mSearchBar.addTextChangedListener(mSearchTextListener);

        mSearchBar.setOnEditorActionListener(mSearchTextListener);

//            mSearchBar.setAdapter();//todo auto compelete

        mSearchProgress = mSearchBar.findViewById(R.id.search_progressbar);


    }

    @Override
    public int getLayoutID() {
        return R.layout.ami_plugin_frame_layout;
    }

    @Override
    public Class<? extends AmiPlugin> bindPlugin() {
        return FramePlugin.class;
    }

    @Override
    public void onTopFrameChanged(IFrame frame, String path) {
        mFrameNavigationPathView.setText(path);
        Object source = frame.getSource();
        mSearchBar.setEnabled(source != null);
        if (source != null) {
            mSearchManager.setSearchTarget(frame.getSource());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mFrameNavigationBackView) {
            mFrameAdapter.navigationUp();
        }
    }

    public void notifyFrameChanged() {
        if (mFrameAdapter != null) {
            mFrameAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearchStarted(String keyword) {
        showSearchProgress();
    }

    @Override
    public void onSearchChanged(String keyword, @Nullable ObjectInfo newObjectInfo, @NonNull ArrayList<ObjectInfo> searchRst) {
        doSearchResult(keyword, searchRst);
    }

    @Override
    public void onSearchFinished(String keyword, @NonNull ArrayList<ObjectInfo> searchRst) {
        doSearchResult(keyword, searchRst);
        hideSearchProgress();
    }

    @Override
    public void onSearchCanceled() {
        hideSearchProgress();
    }


    public boolean onKeyEvent(KeyEvent keyEvent) {
        if (mSearchManager != null) {
            mSearchManager.cancel();
        }
        hideSearchProgress();

        return mFrameAdapter.onBackPressed();
    }

    public void showSearchProgress() {
        if (mSearchProgress == null) {
            mSearchProgress = findView(R.id.search_progressbar);
        }
        mSearchProgress.setVisibility(View.VISIBLE);
    }

    public void hideSearchProgress() {
        if (mSearchProgress == null) {
            mSearchProgress = findView(R.id.search_progressbar);
        }
        mSearchProgress.setVisibility(View.GONE);
    }

    private void doSearchResult(String keyword, ArrayList<ObjectInfo> searchRst) {
        FrameManager frameManager = FrameManager.getInstance();
        FrameImpl frame = frameManager.peek();
        SearchFrame searchFrame = null;
        if (frame instanceof SearchFrame) {
            searchFrame = (SearchFrame) frame;
        } else {
            searchFrame = new SearchFrame(mSearchManager.getSearchTarget());
            frameManager.pushInto(searchFrame);
        }
        searchFrame.setSearchRst(searchRst);
        searchFrame.setSearchText(keyword);
        onTopFrameChanged(searchFrame, frameManager.getPath());
        notifyFrameChanged();
    }
}
