package chao.app.ami.frames;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import chao.app.ami.Ami;
import chao.app.ami.Constants;
import chao.app.debug.R;

/**
 * @author chao.qin
 * @since 2017/8/2
 */

public class FrameAdapter extends RecyclerView.Adapter implements DrawerLayout.DrawerListener {

    private static final int ITEM_VIEW_TYPE_CATEGORY = 1;
    private static final int ITEM_VIEW_TYPE_INFO = 2;

    private Context mContext = Ami.getApp();

    private RecyclerView mFrameView;
    private LinearLayoutManager mLayoutManager;

    private FrameProcessor mFrameProcessor = FrameManager.getInstance().getFrameProcessor();

    public FrameAdapter(RecyclerView recyclerView) {
        mFrameView = recyclerView;
        mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = viewType == ITEM_VIEW_TYPE_CATEGORY ? R.layout.frame_adapter_item_category : R.layout.frame_adapter_item;
        return new RecyclerView.ViewHolder(LayoutInflater.from(mContext).inflate(layoutId, parent, false)) {
        };
    }


    private void toggleSpreader(View itemView) {
        TextView nameView = (TextView) itemView.findViewById(R.id.frame_adapter_item_name);
        TextView valueView = (TextView) itemView.findViewById(R.id.frame_adapter_item_value);
        TextView classView = (TextView) itemView.findViewById(R.id.frame_adapter_item_class_name);
        ImageView spreaderIco = (ImageView) itemView.findViewById(R.id.spreader);

        boolean spread = classView.getVisibility() == View.VISIBLE;
        if (spread) {
            //当前已展开， 收缩
            classView.setVisibility(View.GONE);
            nameView.setMaxLines(1);
            valueView.setMaxLines(1);
            classView.setMaxLines(1);
            spreaderIco.setImageResource(R.drawable.ami_frame_item_shrink);

        } else {
            //当前已收缩， 展开
            classView.setVisibility(View.VISIBLE);
            nameView.setMaxLines(2);
            valueView.setMaxLines(2);
            classView.setMaxLines(2);
            spreaderIco.setImageResource(R.drawable.ami_frame_item_spread);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        FrameImpl frame = mFrameProcessor.peek();
        final ObjectFrame.Entry entry = frame.getEntry(position);
        if (holder.getItemViewType() == ITEM_VIEW_TYPE_INFO) {
            TextView nameView = (TextView) holder.itemView.findViewById(R.id.frame_adapter_item_name);
            TextView valueView = (TextView) holder.itemView.findViewById(R.id.frame_adapter_item_value);
            TextView classView = (TextView) holder.itemView.findViewById(R.id.frame_adapter_item_class_name);
            nameView.setText(entry.title);
            valueView.setText(entry.value);
            classView.setText(entry.object.getClass().getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entry.object == null) {
                        return;
                    }
                    View topView = mLayoutManager.getChildAt(0);
                    int offset = topView.getTop();
                    int position = mLayoutManager.findFirstVisibleItemPosition();
                    mFrameProcessor.pushInto(entry, position, offset);
                    mLayoutManager.scrollToPositionWithOffset(0,0);
                    notifyDataSetChanged();
                }
            });
            View spreader = holder.itemView.findViewById(R.id.spreader);
            spreader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSpreader(holder.itemView);
                }
            });
            return;
        }

        TextView textView = (TextView) holder.itemView;
        textView.setText(entry.title);
    }

    @Override
    public int getItemCount() {
        return mFrameProcessor.peek().getSize();
    }

    @Override
    public int getItemViewType(int position) {
        IFrame frame = mFrameProcessor.peek();
        IFrame.Entry entry = frame.getEntry(position);
        if (Constants.CATEGORY.equals(entry.value)) {
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
}
