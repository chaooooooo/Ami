package chao.app.ami.plugin.plugins.frame.items;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import chao.app.ami.R;
import chao.app.ami.plugin.plugins.frame.IFrame;

/**
 * @author chao.qin
 * @since 2018/8/11
 */

public class ObjectFrameItem extends BaseFrameItem {

    private TextView titleView;
    private TextView valueView;
    private TextView classView;
    private ImageView spreader;

    public ObjectFrameItem(Context context) {
        super(context);
    }

    @Override
    public void bindView() {
        titleView = findViewById(R.id.frame_adapter_item_name);
        valueView = findViewById(R.id.frame_adapter_item_value);
        classView = findViewById(R.id.frame_adapter_item_class_name);
        spreader = findViewById(R.id.spreader);
        spreader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSpreader();
            }
        });
    }

    @Override
    public void bindData(IFrame.Entry entry) {
        titleView.setText(entry.getTitle());
        valueView.setText(String.valueOf(entry.getValue()));
        classView.setText(entry.getClassName());
    }

    private void toggleSpreader() {
        boolean spread = classView.getVisibility() == View.VISIBLE;
        if (spread) {
            //当前已展开， 收缩
            classView.setVisibility(View.GONE);
            titleView.setMaxLines(1);
            valueView.setMaxLines(1);
            classView.setMaxLines(1);
            spreader.setImageResource(R.drawable.ami_frame_item_shrink);

        } else {
            //当前已收缩， 展开
            classView.setVisibility(View.VISIBLE);
            titleView.setMaxLines(2);
            valueView.setMaxLines(2);
            classView.setMaxLines(2);
            spreader.setImageResource(R.drawable.ami_frame_item_spread);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.frame_item_view_object;
    }
}
