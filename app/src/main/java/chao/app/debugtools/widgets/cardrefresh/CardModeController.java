package chao.app.debugtools.widgets.cardrefresh;

import android.view.View;
import chao.app.ami.Ami;

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
        return overHeader() && (cursor() > refreshLine());

    }

    @Override
    public int offset() {
        return headerView.getCardView().getHeight();
    }

    @Override
    public boolean overPull() {
        View cardView = headerView.getCardView();
        if (cardView == null) {
            return false;
        }
        return overHeader() && (cursor() > cardView.getHeight()) && cursor() < refreshLine();
    }

    @Override
    public boolean overCard() {
        if (!overHeader()) {
            return false;
        }
        View cardView = headerView.getCardView();
        if (cardView == null) {
            return false;
        }
        if (cursor() > cardLine() - 20) {
            return false;
        }
        return true;
    }

    @Override
    public void showRefresh() {
        if (!overHeader()) {
            return;
        }
        pullRecycleView.setState(PullRecycleView.State.SHRINKING);
        pullRecycleView.smoothScrollBy(0, refreshView.getHeight() - cursor());
    }

    @Override
    public void allShrink() {
        if (!overHeader()) {
            return;
        }
        pullRecycleView.setState(PullRecycleView.State.SHRINKING);
        int y = cursor() - headerView.getCardView().getHeight();
        pullRecycleView.smoothScrollBy(0, y);
    }

    @Override
    public void resetRefresh() {

    }

    @Override
    public void hideCard() {
        final View cardView = headerView.getCardView();
        if (cardView == null) {
            return;
        }
        pullRecycleView.setState(PullRecycleView.State.SHRINKING);
        int y = cursor();
        pullRecycleView.smoothScrollBy(0, y);
        cardView.setAlpha(0.4f);
    }

    private int refreshLine() {
        return headerView.getCardView().getHeight() + refreshView.getHeight() / 3;
    }

    private int cardLine() {
        return headerView.getCardView().getHeight();
    }

    public void readyHiddenCard() {
        View cardView = headerView.getCardView();
        if (cardView == null) {
            return;
        }
        if (!overCard()) {
            cardView.setAlpha(1.0f);
            return;
        }
        float alpha = (float) cursor()/(float) getCardHight();
        Ami.log("alpha: " + alpha);
        cardView.setAlpha(alpha);
    }

}
