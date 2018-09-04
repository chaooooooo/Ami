package chao.app.debugtools.widgets;

/**
 * @author qinchao
 * @since 2018/8/16
 */

public interface ModeController {

    void setLayoutManager(LayoutManagerHelper layoutManagerHelper);

    void setHeaderView(PullHeaderView2 headerView);

    boolean overHeader();

    boolean overFull();

    boolean overRefresh();

    void showCard();

    boolean overPull();

    boolean headerShow();

    void showRefresh();

    void allShrink();

    void deltaY(int dy);

    int offset();

    int getY();

    void setY(int y);
}
