package chao.app.ami.viewinfo;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

import chao.app.ami.Ami;
import chao.app.ami.Interceptor;
import chao.app.ami.hooks.ViewGroupHook;
import chao.app.ami.hooks.ViewHook;
import chao.app.debug.R;

/**
 * @author chao.qin
 * @since 2017/8/8
 */

public class ViewInterceptor {


    private Interceptor.OnInterceptorListener mListenerInterceptor = new ListenerInterceptor();

    private OnViewTouchedListener mOnViewTouchedListener;
    private OnViewLongClickListener mOnViewLongClickListener;

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
    public void injectListeners(View child) {
        if (child.getId() == R.id.ami_action_list) {
            return;
        }
        View.OnTouchListener srcTouchListener = ViewHook.getOnTouchListener(child);
        View.OnTouchListener hookTouchListener = Interceptor.newInstance(srcTouchListener, View.OnTouchListener.class, mListenerInterceptor);
        child.setOnTouchListener(hookTouchListener);


        if (!(child instanceof ViewGroup)) {
            View.OnClickListener srcClickListener = ViewHook.getOnClickListener(child);
            View.OnClickListener hookClickListener = Interceptor.newInstance(srcClickListener, View.OnClickListener.class, mListenerInterceptor);
            child.setOnClickListener(hookClickListener);

            View.OnLongClickListener srcLongClickListener = ViewHook.getOnLongClickListener(child);
            View.OnLongClickListener hookLongClickListener = Interceptor.newInstance(srcLongClickListener, View.OnLongClickListener.class, mListenerInterceptor);
            child.setOnLongClickListener(hookLongClickListener);
            return;
        }
        ViewGroup vgChild = (ViewGroup) child;

//        vgChild.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
//        vgChild.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
//        vgChild.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        ViewGroup.OnHierarchyChangeListener srcHierarchyListener = ViewGroupHook.getOnHierarchyChangeListener(vgChild);
        ViewGroup.OnHierarchyChangeListener hookHierarchyListener = Interceptor.newInstance(srcHierarchyListener, ViewGroup.OnHierarchyChangeListener.class, mListenerInterceptor);
        vgChild.setOnHierarchyChangeListener(hookHierarchyListener);
        int grandChildrenCount = vgChild.getChildCount();
        for (int i = 0; i < grandChildrenCount; i++) {
            injectListeners(vgChild.getChildAt(i));
        }
    }

    private class ListenerInterceptor implements Interceptor.OnInterceptorListener, ViewGroup.OnHierarchyChangeListener,View.OnTouchListener, View.OnLongClickListener {

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
            injectListeners(child);
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            //do nothing
        }

        @Override
        public boolean onLongClick(View v) {
            Ami.log("onLongClick : " + v);
            if (mOnViewLongClickListener != null) {
                return mOnViewLongClickListener.onViewLongClicked(v);
            }
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Ami.log("onTouch : " + v.getClass().getSimpleName() + ":" + v.getId() + " event: " + event.getAction());
            Ami.log("onTouch : " + v + " event:" + event.getAction());
            if (mOnViewTouchedListener != null) {
                mOnViewTouchedListener.onViewTouched(v, event);
            }
            return false;
        }
    }

    public void setOnViewLongClickListener(OnViewLongClickListener listener) {
        mOnViewLongClickListener = listener;
    }

    public void setOnViewTouchedListener(OnViewTouchedListener listener) {
        mOnViewTouchedListener = listener;
    }

    public interface OnViewTouchedListener {
        void onViewTouched(View view, MotionEvent event);
    }

    public interface OnViewLongClickListener {
        boolean onViewLongClicked(View view);
    }

}
