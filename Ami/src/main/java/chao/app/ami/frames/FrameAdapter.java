package chao.app.ami.frames;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    private FrameProcessor mFrameProcessor = FrameManager.getInstance().getFrameProcessor();

    public FrameAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = viewType == ITEM_VIEW_TYPE_CATEGORY ? R.layout.frame_adapter_item_category : R.layout.frame_adapter_item;
        return new RecyclerView.ViewHolder(LayoutInflater.from(mContext).inflate(layoutId, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IFrame frame = mFrameProcessor.peek();
        final ObjectFrame.Entry entry = frame.getEntry(position);
        if (holder.getItemViewType() == ITEM_VIEW_TYPE_INFO) {
            TextView nameView = (TextView) holder.itemView.findViewById(R.id.frame_adapter_item_name);
            TextView valueView = (TextView) holder.itemView.findViewById(R.id.frame_adapter_item_value);
            nameView.setText(entry.title);
            valueView.setText(entry.value);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entry.object == null) {
                        return;
                    }
                    mFrameProcessor.pushInto(entry);
                    notifyDataSetChanged();
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

    public void navigationUp() {
        mFrameProcessor.popOut();
        notifyDataSetChanged();
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
}
