package chao.app.debugtools.widgets;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import chao.app.ami.Ami;
import chao.app.ami.text.Poetry;
import chao.app.ami.text.TextManager;

/**
 * @author qinchao
 * @since 2018/8/15
 */

public class PullRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_HEADER = 1;

    private static final int ITEM_TYPE_CONTENT = 2;

    private PullRecycleView mRecyclerView;

    private Context mContext;

    private Poetry poetry[];

    public PullRecyclerAdapter(PullRecycleView recyclerView) {
        mContext = recyclerView.getContext();
        mRecyclerView = recyclerView;
        TextManager.init();
        poetry = TextManager.getSPoetry(20);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            return new RecyclerView.ViewHolder(mRecyclerView.getHeaderView()) {};
        }
        return new RecyclerView.ViewHolder(LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false)) {
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        }
        return ITEM_TYPE_CONTENT;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        if (position == 0) {
//            mRecyclerView.resetCardMode();
//        }
        if (position > 0) {
            TextView textView = (TextView) holder.itemView;
            textView.setText(poetry[position - 1].getPoetryName());
        }
        final int i = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ami.log("position: " + i + ", " + v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return poetry.length + 1;
    }

}
