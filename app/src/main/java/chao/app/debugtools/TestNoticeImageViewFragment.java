package chao.app.debugtools;

import android.app.ActionBar;
import android.view.View;
import chao.app.ami.annotations.LayoutID;
import chao.app.ami.base.AMISupportFragment;
import chao.app.debugtools.aicai.widgets.NoticeImageView;

/**
 * @author qinchao
 * @since 2018/8/31
 */
@LayoutID(R.layout.test_notice_image_view_fragment)
public class TestNoticeImageViewFragment extends AMISupportFragment {


    private NoticeImageView mShoppingCartView;

    @Override
    public void setupView(View layout) {
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("TestNoticeImageViewFragment");
        }

        mShoppingCartView = (NoticeImageView) layout.findViewById(R.id.home_cart);
        mShoppingCartView.setMaxTextLength(2);
        mShoppingCartView.setText("1");
        mShoppingCartView.setOnClickListener(new View.OnClickListener() {
            int text = 1;

            @Override
            public void onClick(View v) {
                text = (text + 5) * text;
                mShoppingCartView.setText(String.valueOf(text));
                if (text > 99) {
                    text = 1;
                }

            }
        });
    }
}
