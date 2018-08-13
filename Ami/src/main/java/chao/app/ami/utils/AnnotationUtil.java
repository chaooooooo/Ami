package chao.app.ami.utils;

import android.view.View;

import chao.app.ami.annotations.LayoutID;

/**
 * @author chao.qin
 * @since 2018/8/11
 */

public class AnnotationUtil {

    /**
     * 通过 注解{@link LayoutID}来获取layoutId
     * @param clazz   被注解的class
     * @return layoutId
     */
    public static int getLayoutFromAnnotation(Class<?> clazz) {
        LayoutID layoutID = clazz.getAnnotation(LayoutID.class);
        if (layoutID == null) {
            return View.NO_ID;
        }
        return layoutID.value();
    }
}
