package chao.app.ami.plugin.plugins.viewinterceptor;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import chao.app.ami.Interceptor;
import chao.app.ami.hooks.ViewGroupHook;
import chao.app.ami.hooks.ViewHook;
import chao.app.ami.launcher.drawer.DrawerManager;
import chao.app.ami.utils.Util;
import chao.app.debug.R;
import java.lang.reflect.Method;

/**
 * @author chao.qin
 * @since 2017/8/8
 */

public class ViewInterceptor {

    private OnViewTouchedListener mOnViewTouchedListener;
    private OnViewLongClickListener mOnViewLongClickListener;

    private boolean mInterceptorEnabled = true;

    public ViewInterceptor() {
    }

    /**
     *  注入listeners
     *    - OnTouchListener
     *    - OnHierarchyListener
     *    - OnClickListener
     *    - OnLongClickListener
     *
     *    fixme
     *    这里只能静态注入， 如果在初始化后调用setOnXxxListener会该View的Xxx事件拦截器丢失
     */
    public void injectListeners(ViewGroup parent, View child) {
        if (child.getId() == R.id.ami_action_list) {
            return;
        }
        if (child.getId() == R.id.ami_settings_panel) {
            return;
        }
        if (!mInterceptorEnabled) {
            return;
        }
        //不注入WebView
        if (Util.isWebView(child)) {
            return;
        }
        InterceptorListener listener = new InterceptorListener();
        InterceptorRecord record = new InterceptorRecord(parent, child, listener);
        listener.setInterceptorRecord(record);

        View.OnTouchListener srcTouchListener = record.getTouchListener();
        if (!hasInjected(srcTouchListener)) {
            View.OnTouchListener hookTouchListener = Interceptor.newInstance(srcTouchListener, new Class[] {View.OnTouchListener.class, IViewInterceptor.class}, listener, false);
            child.setOnTouchListener(hookTouchListener);
        }


        if (!(child instanceof ViewGroup)) {
            View.OnClickListener srcClickListener = ViewHook.getOnClickListener(child);
            if (!hasInjected(srcClickListener)) {
                View.OnClickListener hookClickListener = Interceptor.newInstance(srcClickListener, new Class[] {View.OnClickListener.class, IViewInterceptor.class}, listener, true);
                child.setOnClickListener(hookClickListener);
            }

            //这里两个作用
            //1. 注入了longClick事件代理
            //2. 设置了clickable， 使得每个view都可以被点击到 等同于child.setClickable(true)
            View.OnLongClickListener srcLongClickListener = record.getLongClickListener();
            if (!hasInjected(srcLongClickListener)) {
                View.OnLongClickListener hookLongClickListener = Interceptor.newInstance(srcLongClickListener, new Class[]{View.OnLongClickListener.class, IViewInterceptor.class}, listener, true);
                child.setOnLongClickListener(hookLongClickListener);
            }
            return;
        }
        ViewGroup vgChild = (ViewGroup) child;

        ViewGroup.OnHierarchyChangeListener srcHierarchyListener = record.getHierarchyChangeListener();
        if (!hasInjected(srcHierarchyListener)) {
            ViewGroup.OnHierarchyChangeListener hookHierarchyListener = Interceptor.newInstance(srcHierarchyListener, new Class[]{ViewGroup.OnHierarchyChangeListener.class, IViewInterceptor.class}, listener, false);
            vgChild.setOnHierarchyChangeListener(hookHierarchyListener);
            int grandChildrenCount = vgChild.getChildCount();
            for (int i = 0; i < grandChildrenCount; i++) {
                injectListeners(vgChild, vgChild.getChildAt(i));
            }
        }
    }

    public void unInjectListeners(View child) {
        if (child.getId() == R.id.ami_action_list) {
            return;
        }
        if (child.getId() == R.id.ami_settings_panel) {
            return;
        }
        View.OnTouchListener hookTouchListener = ViewHook.getOnTouchListener(child);
        View.OnTouchListener touchListener = Interceptor.getSourceListener(hookTouchListener);
        child.setOnTouchListener(touchListener);


        View.OnLongClickListener hookLongClickListener = ViewHook.getOnLongClickListener(child);
        View.OnLongClickListener longClickListener = Interceptor.getSourceListener(hookLongClickListener);
        child.setOnLongClickListener(longClickListener);
        if (longClickListener == null) {
            child.setLongClickable(false);
        }

        if (!(child instanceof ViewGroup)) {
            return;
        }
        ViewGroup vgChild = (ViewGroup) child;
        ViewGroup.OnHierarchyChangeListener hookHierarchyListener = ViewGroupHook.getOnHierarchyChangeListener(vgChild);
        ViewGroup.OnHierarchyChangeListener hierarchyChangeListener = Interceptor.getSourceListener(hookHierarchyListener);
        vgChild.setOnHierarchyChangeListener(hierarchyChangeListener);

        int grandChildrenCount = vgChild.getChildCount();
        for (int i = 0; i < grandChildrenCount; i++) {
            unInjectListeners(vgChild.getChildAt(i));
        }
    }

    private boolean hasInjected(Object listener) {
        return listener instanceof IViewInterceptor;
    }

    public void setInterceptorEnabled(boolean enabled) {
        mInterceptorEnabled = enabled;
        View layout = DrawerManager.get().getRealView();
        if (layout == null) {
            return;
        }
        if (enabled) {
            injectListeners(null, layout);
        } else {
            unInjectListeners(layout);
        }
    }

    public boolean isInterceptorEnabled() {
        return mInterceptorEnabled;
    }

    class InterceptorListener implements Interceptor.OnInterceptorListener, ViewGroup.OnHierarchyChangeListener,View.OnTouchListener, View.OnLongClickListener {

        private InterceptorRecord mRecord;

        @Override
        public Object onBeforeInterceptor(Object proxy, Method method, Object[] args) {
            Object result = null;
            if ("onChildViewAdded".equals(method.getName())) {
                onChildViewAdded((View)args[0], (View)args[1]);
            } else if ("onChildViewRemoved".equals(method.getName())) {
                onChildViewRemoved((View)args[0], (View)args[1]);
            } else if ("onLongClick".equals(method.getName())) {
                result = onLongClick((View) args[0]);
            } else if ("onTouch".equals(method.getName())) {
                result = onTouch((View)args[0], (MotionEvent) args[1]);
            }
            return result;
        }

        @Override
        public Object onAfterInterceptor(Object proxy, Method method, Object[] args, Object result) {
            return result;
        }

        @Override
        public void onChildViewAdded(View parent, View child) {
            injectListeners((ViewGroup) parent, child);
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            //do nothing
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnViewLongClickListener != null) {
                return mOnViewLongClickListener.onViewLongClicked(mRecord);
            }
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mOnViewTouchedListener != null) {
                return mOnViewTouchedListener.onViewTouched(mRecord, event);
            }
            return false;
        }

        public void setInterceptorRecord(InterceptorRecord record) {
            mRecord = record;
        }

        public InterceptorListener createListener(InterceptorRecord record) {
            InterceptorListener listener = new InterceptorListener();
            listener.setInterceptorRecord(record);
            return listener;
        }
    }

    public void setOnViewLongClickListener(OnViewLongClickListener listener) {
        mOnViewLongClickListener = listener;
    }

    public void setOnViewTouchedListener(OnViewTouchedListener listener) {
        mOnViewTouchedListener = listener;
    }

    public interface OnViewTouchedListener {
        boolean onViewTouched(InterceptorRecord record, MotionEvent event);
    }

    public interface OnViewLongClickListener {
        boolean onViewLongClicked(InterceptorRecord record);
    }

}
