package chao.app.ami.plugin.plugins.frame.items;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import chao.app.ami.R;
import chao.app.ami.plugin.plugins.frame.IFrame;
import chao.app.ami.plugin.plugins.frame.SearchFrame;

/**
 * @author chao.qin
 * @since 2018/8/11
 */
public class SearchFrameItem extends BaseFrameItem {

    private TextView titleView;
    private TextView valueView;
    private TextView classView;
    private ImageView spreader;

    private TextView searchPathView;


    public SearchFrameItem(Context context) {
        super(context);
    }

    @Override
    public void bindView() {
        titleView = findViewById(R.id.frame_adapter_item_name);
        valueView = findViewById(R.id.frame_adapter_item_value);
        classView = findViewById(R.id.frame_adapter_item_class_name);
        spreader = findViewById(R.id.spreader);
        searchPathView = findViewById(R.id.search_result_path);
        spreader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSpreader();
            }
        });
    }

    private void toggleSpreader() {
        boolean spread = searchPathView.getVisibility() == View.VISIBLE;
        if (spread) {
            //当前已展开， 收缩
            titleView.setMaxLines(1);
            valueView.setMaxLines(1);
            classView.setMaxLines(1);
            searchPathView.setVisibility(View.GONE);
            spreader.setImageResource(R.drawable.ami_frame_item_shrink);

        } else {
            //当前已收缩， 展开
            titleView.setMaxLines(2);
            valueView.setMaxLines(2);
            classView.setMaxLines(2);
            searchPathView.setVisibility(View.VISIBLE);
            spreader.setImageResource(R.drawable.ami_frame_item_spread);
        }
    }

    @Override
    public void bindData(IFrame.Entry entry) {
        titleView.setText(entry.getTitle());
        valueView.setText(String.valueOf(entry.getValue()));
        classView.setText(entry.getClassName());
        if (entry instanceof SearchFrame.SearchEntry) {
            SearchFrame.SearchEntry searchEntry = (SearchFrame.SearchEntry) entry;
            searchPathView.setText(searchEntry.getSearchPath());
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.frame_item_view_search;
    }
}
