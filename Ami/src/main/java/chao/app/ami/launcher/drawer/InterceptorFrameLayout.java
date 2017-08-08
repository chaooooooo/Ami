package chao.app.ami.launcher.drawer;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author chao.qin
 * @since 2017/8/8
 */

public class InterceptorFrameLayout extends FrameLayout implements ViewGroup.OnHierarchyChangeListener {

    private ViewInterceptor mInterceptor = new ViewInterceptor();

    public InterceptorFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public InterceptorFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InterceptorFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnHierarchyChangeListener(this);
    }

    public void setInterceptor(ViewInterceptor interceptor) {
        mInterceptor = interceptor;
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        mInterceptor.injectListeners(child);
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        //do nothing
    }
}
