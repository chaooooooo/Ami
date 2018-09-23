package chao.app.ami.plugin.plugins.frame.items;

import android.content.Context;
import android.widget.TextView;

import chao.app.ami.plugin.plugins.frame.IFrame;
import chao.app.debug.R;

/**
 * @author chao.qin
 * @since 2018/8/11
 */

public class CategoryFrameItem extends BaseFrameItem {

    private TextView categoryView;

    public CategoryFrameItem(Context context) {
        super(context);
    }

    @Override
    public void bindView() {
        categoryView = (TextView) getItemView();
    }

    @Override
    public void bindData(IFrame.Entry entry) {
        categoryView.setText(entry.getClassName());
    }

    @Override
    public int getLayoutId() {
        return R.layout.frame_adapter_item_category;
    }
}
