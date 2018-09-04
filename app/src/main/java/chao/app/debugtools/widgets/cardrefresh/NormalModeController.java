package chao.app.debugtools.widgets.cardrefresh;

import chao.app.ami.Ami;

/**
 * @author qinchao
 * @since 2018/8/16
 */

public class NormalModeController extends AbstractModeController {

    public NormalModeController(PullRecycleView recycleView) {
        super(recycleView);
    }

    public boolean overFull() {
        if (!overHeader()) {
            return false;
        }
        return cursor() > fullLine();
    }

    @Override
    public boolean overRefresh() {
        if (!overHeader()) {
            return false;
        }
        return cursor() > refreshLine();
    }

    @Override
    public boolean overPull() {
        return true;
    }

    @Override
    public boolean overCard() {
        //do nothing
        return false;
    }

    @Override
    public void hideCard() {
        //do nothing
    }

    @Override
    public void showRefresh() {
        if (!overHeader()) {
            return;
        }
        pullRecycleView.setState(PullRecycleView.State.SHRINKING);
        int y = getHeight() - headerView.getCardView().getHeight() - cursor();
        Ami.log("showRefresh: cursor:" + cursor());
        Ami.log("showRefresh: y:" + y);
        pullRecycleView.smoothScrollBy(0, -y);

    }

    @Override
    public void allShrink() {
        if (!overHeader()) {
            return;
        }
        pullRecycleView.setState(PullRecycleView.State.SHRINKING);
        pullRecycleView.smoothScrollBy(0, cursor());
    }

    @Override
    public void resetRefresh() {
        if (!overRefresh()) {
            return;
        }
        pullRecycleView.setState(PullRecycleView.State.SHRINKING);
        int y = getHeight() - headerView.getCardView().getHeight() - cursor();
        pullRecycleView.smoothScrollBy(0, -y);
    }

    private int refreshLine() {
        int refreshViewHeight = headerView.getRefreshView().getHeight();
        return refreshViewHeight / 3;
    }

    private int fullLine() {
        int cardHeight = headerView.getCardView().getHeight();
        int refreshHeight = headerView.getRefreshView().getHeight();
        return refreshHeight + cardHeight / 3;
    }
}
