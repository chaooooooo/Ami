package chao.app.ami.plugin.plugins.viewinterceptor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiSettings;
import chao.app.ami.plugin.AmiGeneralComponent;

/**
 * @author qinchao
 * @since 2018/9/12
 */
public class ViewInterceptorPlugin extends AmiPlugin {

    private InterceptorLayerManager interceptorManager;
    private ViewInterceptorSettings settings;

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    public AmiGeneralComponent createComponent() {
        return new ViewInterceptorComponent(this);
    }

    @Override
    public AmiSettings getSettings() {
        settings = new ViewInterceptorSettings();
        return settings;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (settings == null) {
            getSettings();
        }
        interceptorManager = new InterceptorLayerManager();
        interceptorManager.setInterceptorEnabled(settings.isEnabled());
    }

    @Override
    public void onActivityChanged(FragmentActivity activity) {
        super.onActivityChanged(activity);
        interceptorManager.setInterceptorEnabled(settings.isEnabled());
    }

    @Override
    public CharSequence getTitle() {
        return "View Interceptor";
    }

    @Override
    public Object getManager() {
        return interceptorManager;
    }

    @Override
    public void onBindView(AmiContentView contentView) {

    }
}
