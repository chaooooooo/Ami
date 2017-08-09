package chao.app.debugtools;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import chao.app.ami.annotations.LayoutID;
import chao.app.ami.base.AMISupportFragment;
import chao.app.ami.text.TextManager;

/**
 * @author chao.qin
 * @since 2017/8/9
 */
@LayoutID(R.layout.test_list_fragment)
public class TestListFragment extends AMISupportFragment {

    @Override
    public void setupView(View layout) {
        ListView listView = findView(R.id.list);
        listView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_list_item,R.id.ami_text, TextManager.getSPoetry().getContent()));
    }
}
