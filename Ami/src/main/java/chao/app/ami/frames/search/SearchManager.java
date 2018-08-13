package chao.app.ami.frames.search;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;

import java.util.ArrayList;

/**
 * @author chao.qin
 * @since 2018/8/8
 */

public class SearchManager {

    static final SpannableString EMPTY_SPANNABLE_STRING = new SpannableString("");

    private Object mSearchTarget;

    private SearchTask mTask;

    private ObjectProcessor mProcessor;

    private SearchListener mSearchListener;

    private static SearchManager sSearchManager;

    private SearchManager() {
        mProcessor = new ObjectProcessor();
    }

    public static SearchManager getInstance() {
        if (sSearchManager == null) {
            sSearchManager = new SearchManager();
        }
        return sSearchManager;
    }

    @SuppressLint("StaticFieldLeak")
    public class SearchTask extends AsyncTask<Void, ObjectInfo, Void> implements ObjectProcessor.ObjectSearchListener {
        private String keyword;

        private Object target;

        ArrayList<ObjectInfo> searchRst = new ArrayList<>();


        SearchTask(String keyword, Object target) {
            this.keyword = keyword;
            this.target = target;
            searchRst.clear();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mSearchListener != null) {
                mSearchListener.onSearchStarted(keyword);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ObjectInfo parent = new ObjectInfo(target);
            mProcessor.setObjectSearchListener(this);
            mProcessor.startSearch(searchRst, keyword, parent, String.valueOf(hashCode()));
            return null;
        }

        @Override
        protected void onProgressUpdate(ObjectInfo... objectInfos) {
            super.onProgressUpdate();
            if (isCancelled()) {
                return;
            }
            ObjectInfo objectInfo = null;
            if (objectInfos != null && objectInfos.length > 0) {
                objectInfo = objectInfos[0];
            }
            if (mSearchListener != null) {
                mSearchListener.onSearchChanged(keyword, objectInfo, searchRst);
            }
        }

        @Override
        protected void onPostExecute(Void objectInfo) {
            super.onPostExecute(objectInfo);
            if (isCancelled()) {
                return;
            }
            if (mSearchListener != null ) {
                mSearchListener.onSearchFinished(keyword, searchRst);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mProcessor.clearTask(String.valueOf(hashCode()));
        }

        @Override
        public void newValueFound(ObjectInfo objectInfo) {
            /*
             * 搜索结果通知到主线程
             */
            publishProgress(objectInfo);
        }

    }

    private void startSearch(String keyword, Object searchTarget) {
        if (mTask != null) {
            mTask.cancel(true);
            mProcessor.stopSearch(String.valueOf(mTask.hashCode()));
        }
        mTask = new SearchTask(keyword, searchTarget);
        mTask.execute();
    }


    public void cancel() {
        if (mTask != null) {
            mTask.cancel(true);
            mProcessor.stopSearch(String.valueOf(mTask.hashCode()));
            if (mSearchListener != null) {
                mSearchListener.onSearchCanceled();
            }
        }
    }

    public void setSearchTarget(Object target) {
        this.mSearchTarget = target;
    }

    public Object getSearchTarget() {
        return mSearchTarget;
    }

    public void setSearchListener(SearchListener searchListener) {
        this.mSearchListener = searchListener;
    }

    public interface SearchListener {

        void onSearchStarted(String keyword);

        void onSearchChanged(String keyword, @Nullable ObjectInfo newObjectInfo, @NonNull ArrayList<ObjectInfo> searchRst);

        void onSearchFinished(String keyword, @NonNull ArrayList<ObjectInfo> searchRst);

        void onSearchCanceled();
    }

    public void searchKeyword(String keyword) {
        startSearch(keyword, mSearchTarget);
    }
}
