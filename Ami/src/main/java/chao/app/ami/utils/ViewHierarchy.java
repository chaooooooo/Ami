package chao.app.ami.utils;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.Iterator;

/**
 * @author qinchao
 * @since 2018/9/24
 */
public class ViewHierarchy {

    public static class Builder {

        private View mView;
        
        private View mPoint;

        private Builder(View view) {
            mView = view;
            mPoint = mView;
        }

        @SuppressWarnings("unchecked")
        public <T extends ViewParent> T parent() {
            return (T) mView.getParent();
        }

        @SuppressWarnings("unchecked")
        private  <T extends ViewParent> T nextParent() {
            return (T) mPoint.getParent();
        }

        public ViewFamily<View> ancestors() {
            return new ViewFamily<>(new Itr<View>() {
                @Override
                public void onCreate() {
                    mPoint = mView;
                }

                @Override
                public boolean hasNext() {
                    View parent = nextParent();
                    return parent != null && parent instanceof ViewGroup;
                }

                @Override
                public View next() {
                    ViewGroup parent = nextParent();
                    mPoint = parent;
                    return parent;
                }
            });
        }


        public ViewFamily<View> brothers() {

            return new ViewFamily<>(new Itr<View>() {

                int index = 0;

                int childCount = 0;

                ViewGroup parent = parent();

                {
                    if (parent != null) {
                        childCount = parent.getChildCount();
                    }
                }

                @Override
                public void onCreate() {
                    index = 0;
                }

                @Override
                public boolean hasNext() {
                    return parent != null
                        && childCount > 0
                        && index < childCount;
                }

                @Override
                public View next() {
                    View brother = parent.getChildAt(index);
                    index++;
                    return brother;
                }
            });
        }
        
    }
    
    public static class ViewFamily<V extends View> implements Iterable<V> {
        
        private Itr<V> itr;

        ViewFamily(Itr<V> itr) {
            this.itr = itr;
        }

        public V filter(ViewFilter<V> filter) {
            for (V v: this) {
                if(filter.onFilter(v)) {
                    return v;
                }
            }
            return null;
        }

        @NonNull
        @Override
        public Iterator<V> iterator() {
            itr.onCreate();
            return new Iterator<V>() {
                @Override
                public boolean hasNext() {
                    return itr.hasNext();
                }

                @Override
                public V next() {
                    return itr.next();
                }
            };
        }
    }

    public abstract static class Itr<V> implements Iterator<V> {
        public abstract void onCreate();
    }

    public interface ViewFilter<V> {
        boolean onFilter(V v);
    }

    public static Builder of(View view) {
        return new Builder(view);
    }

}
