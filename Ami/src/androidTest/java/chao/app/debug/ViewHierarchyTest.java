package chao.app.debug;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import chao.app.ami.Ami;
import chao.app.ami.utils.ViewHierarchy;
import java.util.function.Consumer;
import org.junit.Before;
import org.junit.Test;

/**
 * @author qinchao
 * @since 2018/9/25
 */
public class ViewHierarchyTest extends BaseTest {

    private View mView;

    @Before
    public void init() {
        Context context = Ami.getApp();
        mView = new View(context);
        View brother1 = new View(context);
        View brother2 = new View(context);
        View brother3 = new View(context);
        View brother4 = new View(context);
        View brother5 = new View(context);

        brother1.setTag("brother1");
        brother2.setTag("brother2");
        brother3.setTag("brother3");
        brother4.setTag("brother4");
        brother5.setTag("brother5");

        ViewGroup parent = new LinearLayout(context);
        parent.addView(mView);
        parent.addView(brother1);
        parent.addView(brother2);
        parent.addView(brother3);

        parent.setTag("parent");

        ViewGroup parentBrother = new LinearLayout(context);
        parentBrother.addView(brother4);
        parentBrother.addView(brother5);
        parentBrother.setTag("parentBrother");

        ViewGroup pprant = new RelativeLayout(context);
        pprant.addView(parent);
        pprant.addView(parentBrother);

        pprant.setTag("pparent");
    }


    @Test
    public void testAncestors() {

        ViewGroup parent = ViewHierarchy.of(mView).parent();

        assert parent != null;

        assert "parent".equals(parent.getTag());

        int ancestors = 0;
        for (View view: ViewHierarchy.of(mView).ancestors()) {
            assert view != null;
            ancestors ++;
        }
        assert ancestors == 2;

        ViewGroup pparent = ViewHierarchy.of(mView).ancestors().filter(new ViewHierarchy.ViewFilter<ViewGroup>() {
            @Override
            public boolean onFilter(ViewGroup viewGroup) {
                return viewGroup instanceof RelativeLayout;
            }
        });
        assert "pparent".equals(pparent.getTag());
    }

    @Test
    public void testBrothers() {
        ViewHierarchy.ViewFamily<View> brothers = ViewHierarchy.of(mView).brothers();
        int i = 0;
        for (View v: brothers) {
            assert v != null;
            i++;
        }

        assert i == 4;//自己 brother1，2，3

        boolean result = brothers.filter(new ViewHierarchy.ViewFilter<View>() {
            @Override
            public boolean onFilter(View view) {
                return "brother2".equals(view.getTag());
            }
        }).getTag().equals("brother2");

        assert result;

        result = brothers.filter(new ViewHierarchy.ViewFilter<View>() {
            @Override
            public boolean onFilter(View view) {
                return "brother2".equals(view.getTag());
            }
        }).getTag().equals("brother2");
        assert result;

        result = brothers.filter(new ViewHierarchy.ViewFilter<View>() {
            @Override
            public boolean onFilter(View view) {
                return "brother3".equals(view.getTag());
            }
        }).getTag().equals("brother3");
        assert result;
    }

    @Test
    public void testIterator() {
        ViewHierarchy.of((View) ViewHierarchy.of(mView).parent()).brothers().forEach(new Consumer<View>() {
            @Override
            public void accept(View view) {
                assert  view != null;
                Ami.log(view);
            }
        });
    }

}
