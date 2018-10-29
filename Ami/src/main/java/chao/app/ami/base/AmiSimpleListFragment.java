package chao.app.ami.base;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import chao.app.ami.R;
import java.util.List;



public abstract class AmiSimpleListFragment extends AMISupportFragment implements AdapterView.OnItemClickListener {

    @Override
    public void setupView(View layout) {
        ListView listView = findView(R.id.ami_simple_list);
        listView.setOnItemClickListener(this);
        Object object = getObjects();
        if (object instanceof Object[]) {
            Object[] array = (Object[]) object;
            listView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array));
        } else if (object instanceof List) {
            List list = (List) object;
            listView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list));
        }

    }

    @Override
    public int getLayoutID() {
        return R.layout.ami_simple_list_fragment;
    }

    public abstract Object getObjects();
}
