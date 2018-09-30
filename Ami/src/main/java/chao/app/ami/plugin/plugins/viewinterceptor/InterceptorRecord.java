package chao.app.ami.plugin.plugins.viewinterceptor;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import chao.app.ami.hooks.ViewGroupHook;
import chao.app.ami.hooks.ViewHook;

/**
 * @author chao.qin
 * @since 2017/8/13
 */

public class InterceptorRecord {

    View view;
    ViewGroup parent;

    ViewInterceptor.InterceptorListener listener;

    private View.OnTouchListener sourceTouchListener;
    private View.OnLongClickListener sourceLongClickListener;
    private View.OnClickListener sourceClickListener;
    private ViewGroup.OnHierarchyChangeListener sourceHierarchyChangeListener;

    public InterceptorRecord(ViewGroup parent, View view, ViewInterceptor.InterceptorListener listener) {
        this.view = view;
        this.listener = listener;
        this.parent = parent;

        View.OnClickListener click = ViewHook.getOnClickListener(view);
        if (!(click instanceof IViewInterceptor)) {
            sourceClickListener = click;
        }
        View.OnLongClickListener longClick = ViewHook.getOnLongClickListener(view);
        if (!(longClick instanceof IViewInterceptor)) {
            sourceLongClickListener = longClick;
        }
        View.OnTouchListener touch = ViewHook.getOnTouchListener(view);
        if (!(touch instanceof IViewInterceptor)) {
            sourceTouchListener = touch;
        }
        if (view instanceof ViewGroup) {
            ViewGroup.OnHierarchyChangeListener hierarchy = ViewGroupHook.getOnHierarchyChangeListener((ViewGroup) view);
            if (!(hierarchy instanceof IViewInterceptor)) {
                sourceHierarchyChangeListener = hierarchy;
            }
        }
    }

    public View.OnTouchListener getTouchListener() {
        if (view == null) {
            return null;
        }
        return ViewHook.getOnTouchListener(view);
    }

    public View.OnClickListener getClickListener() {
        if (view == null) {
            return null;
        }
        return ViewHook.getOnClickListener(view);
    }

    public View.OnLongClickListener getLongClickListener() {
        if (view == null) {
            return null;
        }
        return ViewHook.getOnLongClickListener(view);
    }

    public ViewGroup.OnHierarchyChangeListener getHierarchyChangeListener() {
        if (view == null) {
            return null;
        }
        if (view instanceof ViewGroup) {
            return ViewGroupHook.getOnHierarchyChangeListener((ViewGroup) view);
        }
        return null;
    }

    public View.OnLongClickListener getSourceLongClickListener() {
        return sourceLongClickListener;
    }

    public View.OnClickListener getSourceClickListener() {
        return sourceClickListener;
    }

    public View.OnTouchListener getSourceTouchListener() {
        return sourceTouchListener;
    }

    public ViewGroup.OnHierarchyChangeListener getSourceHierarchyChangeListener() {
        return sourceHierarchyChangeListener;
    }

    public InterceptorRecord getParentRecord() {
        if (parent == null) {
            return null;
        }
        ViewParent viewParent = parent.getParent();
        if (!(viewParent instanceof ViewGroup)) {
            return null;
        }
        ViewGroup vg = (ViewGroup) viewParent;
        if (vg.getId() == android.R.id.content) {
            return null;
        }
        InterceptorRecord record = new InterceptorRecord(vg, parent, null);
        record.listener = listener.createListener(record);
        return record;
    }
}
