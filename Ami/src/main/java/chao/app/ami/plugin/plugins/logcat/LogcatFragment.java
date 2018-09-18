package chao.app.ami.plugin.plugins.logcat;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiPluginFragment;
import chao.app.ami.plugin.MovementTouch;
import chao.app.debug.R;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qinchao
 * @since 2018/9/4
 */
public class LogcatFragment extends AmiPluginFragment implements View.OnClickListener {


    private RecyclerView logcatView;

    private LogcatAdapter mAdapter;


    private LogcatManager logcatManager;

    private LogcatSettings logcatSettings;


    private ArrayList<LogcatLine> caches = new ArrayList<>();

    private View clearView;

    private ToggleButton pauseView;

    private ToggleButton heartView;

    private View zoomIn;

    private View zoomOut;

    public LogcatFragment() {
        logcatManager = getManager();
        logcatSettings = getSettings();
    }

    @Override
    public void setupView(View layout) {
        super.setupView(layout);

        logcatView = findView(R.id.logcat_list);
        logcatView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mAdapter = new LogcatAdapter();
        logcatView.setAdapter(mAdapter);
        logcatView.setLayoutManager(new LinearLayoutManager(getContext()));

        clearView = findView(R.id.ami_plugin_logcat_settings_clear);
        pauseView = findView(R.id.ami_plugin_logcat_settings_pause);
        heartView = findView(R.id.ami_plugin_logcat_settings_heart);
        zoomIn = findView(R.id.ami_plugin_logcat_settings_zoom_in);
        zoomOut = findView(R.id.ami_plugin_logcat_settings_zoom_out);

        View zoomPane = findView(R.id.ami_plugin_logcat_zoom_panel);
        zoomPane.setOnTouchListener(new MovementTouch(zoomPane));

        clearView.setOnClickListener(this);
        pauseView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                logcatSettings.setPause(!isChecked);
            }
        });
        heartView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                logcatSettings.setHeart(isChecked);
                if (isChecked) {
                    logcatManager.notifyHeartBreak();
                } else {
                    logcatManager.cancelNotifyHeartBreak();
                }
            }
        });
        zoomIn.setOnClickListener(this);
        zoomOut.setOnClickListener(this);
    }

    @Override
    public Class<? extends AmiPlugin> bindPlugin() {
        return LogcatPlugin.class;
    }

    public void notifyDataSetCleared() {
        caches.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(ArrayList<LogcatLine> logCaches, int pos, int length) {
        if (mAdapter == null) {
            return;
        }
        if (logCaches == null || length == 0) {
            return;
        }
        caches.clear();
        caches.addAll(logCaches);
        if (logCaches.size() == 0) {
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v == clearView) {
            logcatManager.clear();
        } else if (v == zoomIn) {
            logcatSettings.zoomIn();
            mAdapter.notifyDataSetChanged();
        } else if (v == zoomOut) {
            logcatSettings.zoomOut();
            mAdapter.notifyDataSetChanged();
        }
    }

    private class LogcatAdapter extends RecyclerView.Adapter implements View.OnClickListener {

        private float orgTextSize;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.ami_plugin_logcat_item_layout, parent, false);
            TextView logText = (TextView) itemView.findViewById(R.id.log_text);
            orgTextSize = logText.getTextSize();
            return new RecyclerView.ViewHolder(itemView) {
            };

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView logText = (TextView) holder.itemView.findViewById(R.id.log_text);
            TextView timestampView = (TextView) holder.itemView.findViewById(R.id.ami_plugin_logcat_item_timestamp);
            TextView processView = (TextView) holder.itemView.findViewById(R.id.ami_plugin_logcat_item_process);

            LogcatLine logcat = caches.get(position);
            String log = logcat.getLog();
            int textColor = logcat.getLevel().getColor();
            logText.setText(log);
            logText.setTextColor(textColor);
            logText.setTextSize(TypedValue.COMPLEX_UNIT_PX, orgTextSize + logcatSettings.getZoom());


            String timestamp = logcat.getDate() + "-" + logcat.getTime();
            String process = getProcessName(getContext(), Integer.valueOf(logcat.getPid())) + "(" + logcat.getPid() + "/" + logcat.getTid() + ")";
            timestampView.setText(timestamp);
            timestampView.setTextColor(textColor);
            processView.setText(process);
            processView.setTextColor(textColor);

            holder.itemView.setOnClickListener(this);
        }

        public String getProcessName(Context cxt, int pid) {
            ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps == null) {
                return null;
            }
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return caches.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public void onClick(View v) {
            View view = v.findViewById(R.id.ami_plugin_logcat_item_attaches);
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.ami_plugin_logcat_fragment;
    }
}
