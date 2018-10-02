package chao.app.debugtools.apps;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import chao.app.ami.annotations.LayoutID;
import chao.app.ami.base.AMISupportFragment;
import chao.app.debugtools.R;
import java.util.List;

/**
 * @author qinchao
 * @since 2018/9/14
 */
@LayoutID(R.layout.fragment_apps_list)
public class AppsFragment extends AMISupportFragment implements View.OnClickListener {

    private RecyclerView appList;

    private List<ResolveInfo> resolveInfos;

    private Button button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolveInfos = PackageUtils.queryActivities(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.aicai.axd", "com.aiyoumi.biopsy.view.ocr.OCRMainActivity"));
        startActivity(intent);
    }

    @Override
    public void setupView(View layout) {

        button = findView(R.id.btn);
        button.setOnClickListener(this);

        appList = findView(R.id.app_list);
        appList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        appList.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                TextView title = new TextView(getContext());
                return new RecyclerView.ViewHolder(title) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TextView app = (TextView) holder.itemView;
//                app.setText(resolveInfos.get(position));
            }

            @Override
            public int getItemCount() {
                return resolveInfos.size();
            }
        });
    }
}
