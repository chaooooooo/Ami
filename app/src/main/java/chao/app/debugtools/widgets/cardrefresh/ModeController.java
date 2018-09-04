package chao.app.debugtools.widgets.cardrefresh;

/**
 * @author qinchao
 * @since 2018/8/16
 */

public interface ModeController {

    void setHeaderView(PullHeaderView headerView);

    boolean overHeader();

    boolean overFull();

    boolean overRefresh();

    boolean overPull();

    boolean overCard();

    void hideCard();

    void showRefresh();

    void resetRefresh();

    void allShrink();


    int cursor();

    int offset();
}
