package chao.app.ami.viewinfo;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

import chao.app.ami.Ami;
import chao.app.ami.Interceptor;
import chao.app.debug.R;

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
        InterceptorListener listener = new InterceptorListener();
        InterceptorRecord record = new InterceptorRecord(parent, child, listener);
        listener.setInterceptorRecord(record);

        View.OnTouchListener srcTouchListener = record.getTouchListener();
        View.OnTouchListener hookTouchListener = Interceptor.newInstance(srcTouchListener, new Class[]{View.OnTouchListener.class, IViewInterceptor.class}, listener, false);
        child.setOnTouchListener(hookTouchListener);


        if (!(child instanceof ViewGroup)) {
//            View.OnClickListener srcClickListener = ViewHook.getOnClickListener(child);
//            View.OnClickListener hookClickListener = Interceptor.newInstance(srcClickListener, View.OnClickListener.class, mListenerInterceptor);
//            child.setOnClickListener(hookClickListener);

            View.OnLongClickListener srcLongClickListener = record.getLongClickListener();
            if (!(srcLongClickListener instanceof IViewInterceptor)) {
                View.OnLongClickListener hookLongClickListener = Interceptor.newInstance(srcLongClickListener, new Class[]{View.OnLongClickListener.class, IViewInterceptor.class}, listener, true);
                child.setOnLongClickListener(hookLongClickListener);
            }
            return;
        }
        ViewGroup vgChild = (ViewGroup) child;

        ViewGroup.OnHierarchyChangeListener srcHierarchyListener = record.getHierarchyChangeListener();
        if (!(srcHierarchyListener instanceof IViewInterceptor)) {
            ViewGroup.OnHierarchyChangeListener hookHierarchyListener = Interceptor.newInstance(srcHierarchyListener, new Class[]{ViewGroup.OnHierarchyChangeListener.class, IViewInterceptor.class}, listener, false);
            vgChild.setOnHierarchyChangeListener(hookHierarchyListener);
            int grandChildrenCount = vgChild.getChildCount();
            for (int i = 0; i < grandChildrenCount; i++) {
                injectListeners(vgChild, vgChild.getChildAt(i));
            }
        }
    }

    public void setInterceptorEnabled(boolean enabled) {
        mInterceptorEnabled = enabled;
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
        public Object onAfterInterceptor(Object proxy, Method method, Object[] args) {
            return null;
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
            Ami.log("onLongClick : " + v);
            if (mOnViewLongClickListener != null) {
                return mOnViewLongClickListener.onViewLongClicked(mRecord);
            }
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mOnViewTouchedListener != null) {
                mOnViewTouchedListener.onViewTouched(mRecord, event);
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
        void onViewTouched(InterceptorRecord record, MotionEvent event);
    }

    public interface OnViewLongClickListener {
        boolean onViewLongClicked(InterceptorRecord record);
    }

}
