package chao.app.ami.frames.search;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import chao.app.ami.frames.Constants;


/**
 * @author chao.qin
 * @since 2018/8/10
 */

public class SearchTextListener implements TextWatcher, TextView.OnEditorActionListener {

    private SearchManager mSearchManager;

    private TextHandler mTextHandler;

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchText(v.getText());
            return true;
        }
        return false;
    }


    private class TextHandler extends Handler {

        private long mLastTimeStamp;

        private static final int WHAT_KEYWORD_CHANGED = 1;

        private TextHandler(Looper looper) {
            super(looper);
        }

        /**
         * 以 {@link Constants#SEARCH_KEYWORD_INTERVAL } 为间隔，更新搜索关键字
         *
         * @param keyword 搜索关键字
         */
        private void sendKeywordChanged(String keyword) {
            cancelKeywordChanged();
            long current = SystemClock.elapsedRealtime();
            long interval = current - mLastTimeStamp;
            long deltaTime = Math.max(Constants.SEARCH_KEYWORD_INTERVAL - interval, 0);
            mLastTimeStamp = current;
            Message message = obtainMessage(WHAT_KEYWORD_CHANGED);
            message.obj = keyword;
            sendMessageDelayed(message, deltaTime);
        }

        private void cancelKeywordChanged(){
            removeMessages(WHAT_KEYWORD_CHANGED);
        }


       @Override
       public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_KEYWORD_CHANGED:
                    String keyword = String.valueOf(msg.obj);
                    mSearchManager.searchKeyword(keyword);
                    break;
                default:
                    break;
            }
       }
    }


    public SearchTextListener(SearchManager searchManager) {
        mSearchManager = searchManager;
        mTextHandler = new TextHandler(Looper.getMainLooper());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        searchText(s);
    }

    private void searchText(CharSequence s) {
        String keyword = String.valueOf(s);
        if (keyword.length() <= Constants.MIN_SEARCH_LENGTH) {
            return;
        }
        mTextHandler.sendKeywordChanged(keyword);
    }
}
