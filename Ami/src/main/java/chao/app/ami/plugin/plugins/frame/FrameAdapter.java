package chao.app.ami.plugin.plugins.frame;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import chao.app.ami.Ami;
import chao.app.ami.plugin.plugins.frame.items.CategoryFrameItem;
import chao.app.ami.plugin.plugins.frame.items.FrameItem;
import chao.app.ami.plugin.plugins.frame.items.ObjectFrameItem;
import chao.app.ami.plugin.plugins.frame.items.SearchFrameItem;
import chao.app.ami.plugin.plugins.frame.search.SearchManager;

/**
 * @author chao.qin
 * @since 2017/8/2
 */

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.FrameViewHolder> implements DrawerLayout.DrawerListener {

    private static final int ITEM_VIEW_TYPE_CATEGORY = 1;
    private static final int ITEM_VIEW_TYPE_INFO = 2;
    private static final int ITEM_VIEW_TYPE_SEARCH = 3;

    private Context mContext = Ami.getApp();

    private RecyclerView mFrameView;
    private LinearLayoutManager mLayoutManager;

    private FrameProcessor mFrameProcessor = FrameManager.getInstance().getFrameProcessor();

    public FrameAdapter(RecyclerView recyclerView) {
        mFrameView = recyclerView;
        mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    @Override
    public FrameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        int layoutId = viewType == ITEM_VIEW_TYPE_CATEGORY ? R.layout.frame_adapter_item_category : R.layout.frame_item_view_object;
//        switch (viewType) {
//            case ITEM_VIEW_TYPE_CATEGORY:
//                layoutId = R.layout.frame_adapter_item_category;
//                break;
//            case ITEM_VIEW_TYPE_INFO:
//                layoutId = R.layout.frame_item_view_object;
//                break;
//            case ITEM_VIEW_TYPE_SEARCH:
//                layoutId = R.layout.frame_item_view_search;
//                break;
//        }
//        return new RecyclerView.ViewHolder(LayoutInflater.from(mAppContext).inflate(layoutId, parent, false)) {
//        };
        FrameItem frameItem;
        switch (viewType) {
            case ITEM_VIEW_TYPE_CATEGORY:
                frameItem = new CategoryFrameItem(mContext);
                break;
            case ITEM_VIEW_TYPE_INFO:
                frameItem = new ObjectFrameItem(mContext);
                break;
            case ITEM_VIEW_TYPE_SEARCH:
                frameItem = new SearchFrameItem(mContext);
                break;
            default:
                throw new IllegalArgumentException("unknown view type.");
        }

        frameItem.initView(parent);
        return new FrameViewHolder(frameItem);
    }

    class FrameViewHolder extends RecyclerView.ViewHolder {

        private FrameItem frameItem;

        FrameViewHolder(FrameItem frameItem) {
            super(frameItem.getItemView());
            this.frameItem = frameItem;
        }
    }


    @Override
    public void onBindViewHolder(final FrameViewHolder holder, int position) {
        final FrameImpl frame = mFrameProcessor.peek();
        final ObjectFrame.Entry entry = frame.getEntry(position);

        holder.frameItem.bindView();
        holder.frameItem.bindData(frame.getEntry(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entry.isCategory() || entry.value == null) {
                        return;
                    }
                    View topView = mLayoutManager.getChildAt(0);
                    int offset = topView.getTop();
                    int position = mLayoutManager.findFirstVisibleItemPosition();
                    frame.setOffset(offset);
                    frame.setPosition(position);
                    if (mFrameProcessor.pushInto(entry)) {
                        SearchManager.getInstance().cancel();
                    }
                    mLayoutManager.scrollToPositionWithOffset(0,0);
                    notifyDataSetChanged();
                }
            });
    }

    @Override
    public int getItemCount() {
        return mFrameProcessor.peek().getSize();
    }

    @Override
    public int getItemViewType(int position) {
        IFrame frame = mFrameProcessor.peek();
        if (frame instanceof SearchFrame) {
            return ITEM_VIEW_TYPE_SEARCH;
        }
        IFrame.Entry entry = frame.getEntry(position);
        if (entry.isCategory) {
            return ITEM_VIEW_TYPE_CATEGORY;
        }
        return ITEM_VIEW_TYPE_INFO;
    }

    public IFrame navigationUp() {
        IFrame frame = mFrameProcessor.popOut();
        notifyDataSetChanged();
        restoreOffset();
        return frame;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        if (newState == DrawerLayout.STATE_DRAGGING) {
            notifyDataSetChanged();
        }
    }

    public void restoreOffset() {
        int position = mFrameProcessor.peek().getPosition();
        int offset = mFrameProcessor.peek().getOffset();
        mLayoutManager.scrollToPositionWithOffset(position, offset);
    }

    public boolean onBackPressed() {
        return navigationUp() != null;
    }

    public void searchText(String keyword) {

    }
}
