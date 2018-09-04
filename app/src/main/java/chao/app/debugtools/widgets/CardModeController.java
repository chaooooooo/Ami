package chao.app.debugtools.widgets;

/**
 * @author qinchao
 * @since 2018/8/16
 */

public class CardModeController extends AbstractModeController{

    public CardModeController(PullRecycleView recycleView) {
        super(recycleView);
    }

    @Override
    public boolean overFull() {
        return false;
    }

    @Override
    public boolean overRefresh() {
        return overHeader() && (Math.abs(offset()) >= Math.abs(refreshLine()));

    }

    @Override
    public void showCard() {
//        allShrink();
    }

    @Override
    public boolean overPull() {
        return overHeader() && (Math.abs(offset()) < Math.abs(refreshLine()));
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
        return mY - refreshView.getHeight();
    }

    private int refreshLine() {
        return -(refreshView.getHeight() * 2 / 3);
    }

}
