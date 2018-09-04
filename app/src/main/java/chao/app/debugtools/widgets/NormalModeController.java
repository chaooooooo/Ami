package chao.app.debugtools.widgets;

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
        return Math.abs(offset()) > Math.abs(fullLine());
    }

    @Override
    public boolean overRefresh() {
        return Math.abs(offset()) > Math.abs(refreshLine());
    }

    @Override
    public void showCard() {
        if (!overHeader()) {
            return;
        }
        pullRecycleView.smoothScrollBy(0, -headerView.getHeight() - offset());
    }

    @Override
    public boolean overPull() {
        return true;
    }

    @Override
    public void showRefresh() {
        if (!overHeader()) {
            return;
        }
        pullRecycleView.smoothScrollBy(0, -refreshView.getHeight() - offset());

    }

    @Override
    public int offset() {
        return mY - headerView.getHeight();
    }

    private int refreshLine() {
        int refreshViewHeight = headerView.getRefreshView().getHeight();
        return -(refreshViewHeight * 2/ 3);
    }

    private int fullLine() {
        int cardHeight = headerView.getCardView().getHeight();
        int refreshHeight = headerView.getRefreshView().getHeight();
        return -(refreshHeight + cardHeight / 3);
    }
}
