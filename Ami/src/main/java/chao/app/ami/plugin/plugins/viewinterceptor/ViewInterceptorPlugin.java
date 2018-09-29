package chao.app.ami.plugin.plugins.viewinterceptor;

import android.support.v4.app.FragmentActivity;
import chao.app.ami.base.AmiContentView;
import chao.app.ami.plugin.AmiPlugin;
import chao.app.ami.plugin.AmiPluginFragment;

/**
 * @author qinchao
 * @since 2018/9/12
 */
public class ViewInterceptorPlugin extends AmiPlugin<AmiPluginFragment, ViewInterceptorSettings, ViewInterceptorPane> {

    private InterceptorLayerManager interceptorManager;
    private ViewInterceptorSettings settings;

    @Override
    protected AmiPluginFragment createFragment() {
        return null;
    }

    @Override
    public ViewInterceptorPane createComponent() {
        return new ViewInterceptorPane(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        settings = getSettings();
        interceptorManager = new InterceptorLayerManager();
        interceptorManager.setInterceptorEnabled(settings.isEnabled());
    }

    @Override
    public ViewInterceptorSettings createSettings() {
        return new ViewInterceptorSettings();
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
