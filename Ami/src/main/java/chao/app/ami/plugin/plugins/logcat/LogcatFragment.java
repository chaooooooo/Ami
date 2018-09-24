package chao.app.ami.plugin.plugins.logcat;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Process;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiPluginFragment;
import chao.app.ami.plugin.MovementTouch;
import chao.app.ami.utils.Util;
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

    private TextView searchTitleView;

    SearchParam searchParam;

    public LogcatFragment() {
        logcatManager = getManager();
        logcatSettings = getSettings();
        searchParam = new SearchParam();
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

        searchTitleView = findView(R.id.ami_plugin_logcat_settings_search_title);
        searchTitleView.setOnClickListener(this);

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

    public void notifyDataSetChanged(ArrayList<LogcatLine> logCaches) {
        if (mAdapter == null) {
            return;
        }
        if (logCaches == null) {
            return;
        }
        caches.clear();
        caches.addAll(logCaches);
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
        } else if (v == searchTitleView) {
            endSearch();
        }
    }

    private class LogcatAdapter extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {

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
            TextView threadView = (TextView) holder.itemView.findViewById(R.id.ami_plugin_logcat_item_thread);

            LogcatLine logcat = caches.get(position);
            String log = logcat.getLog();
            ColorStateList textColor = logcat.getLevel().getColor();
            logText.setText(log);
            logText.setTextColor(textColor);
            logText.setTextSize(TypedValue.COMPLEX_UNIT_PX, orgTextSize + logcatSettings.getZoom());


            String timestamp = logcat.getDate() + "-" + logcat.getTime();
            String process = getProcessName(getContext(), Integer.valueOf(logcat.getPid())) + "(" + logcat.getPid() + ")";
            String thread = threadName(logcat.getTid()) + "(" + logcat.getTid() + ")";
            timestampView.setText(timestamp);
            timestampView.setTextColor(textColor);
            processView.setText(process);
            processView.setTextColor(textColor);
            threadView.setText(thread);
            threadView.setTextColor(textColor);

            holder.itemView.setOnClickListener(this);
            holder.itemView.setTag(logcat);
            holder.itemView.setOnLongClickListener(this);
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

        @Override
        public boolean onLongClick(View v) {
            showPopupMenu(v);
            return true;
        }
    }

    private View anchorView;

    @SuppressLint("RestrictedApi")
    private void showPopupMenu(View view) {
        // 这里的view代表popupMenu需要依附的view
        anchorView = view;
        PopupMenu popupMenu = new PopupMenu(getContext(), anchorView);
        MenuBuilder menu = (MenuBuilder) popupMenu.getMenu();
        popupMenu.getMenuInflater().inflate(R.menu.ami_menu_logcat, menu);
        LogcatLine logcat = (LogcatLine) view.getTag();
        if (logcat == null) {
            return;
        }
        for (MenuItem item: menu.getNonActionItems()) {
            int itemId = item.getItemId();
            if (itemId == R.id.byTag) {
                item.setTitle("过滤tag: " + logcat.getTag());
            } else if (itemId == R.id.byTid) {
                item.setTitle("过滤线程: " + threadName(logcat.getTid()));
            } else if (itemId == R.id.byLogLv) {
                item.setTitle("过滤log级别: " + logcat.getLevel().name());
            }
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                LogcatLine logcat = (LogcatLine) anchorView.getTag();
                if (logcat == null) {
                    return false;
                }
                // 控件每一个item的点击事件
                int itemId = item.getItemId();
                if (itemId == R.id.byTag) {
                    searchParam.setTag(logcat.getTag());
                } else if (itemId == R.id.byTid) {
                    searchParam.setTid(logcat.getTid());
                } else if (itemId == R.id.byLogLv) {
                    searchParam.setLevel(logcat.getLevel());
                }
                startSearch();
                return true;
            }
        });
    }

    private void startSearch() {
        searchTitleView.setText(searchParam.searchTitle());
        searchTitleView.setVisibility(View.VISIBLE);
        logcatManager.startSearch(searchParam);
    }

    private void endSearch() {
        searchTitleView.setVisibility(View.INVISIBLE);
        searchParam.reset();
        logcatManager.endSearch();
    }

    @Override
    public int getLayoutID() {
        return R.layout.ami_plugin_logcat_fragment;
    }

    private String mainThreadName;

    public String threadName(String pid) {
        if (mainThreadName == null) {
            mainThreadName = Util.getThreadName(String.valueOf(Process.myPid()));
        }
        String name = Util.getThreadName(pid);
        if (mainThreadName.equals(name)) {
            return "main";
        }
        return name;
    }
}
