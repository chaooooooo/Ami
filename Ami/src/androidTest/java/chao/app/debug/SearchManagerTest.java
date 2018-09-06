package chao.app.debug;

import android.app.Application;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.Test;

import java.util.ArrayList;

import chao.app.ami.Ami;
import chao.app.ami.frames.search.ObjectInfo;
import chao.app.ami.frames.search.SearchManager;

/**
 * @author chao.qin
 * @since 2018/8/10
 */

public class SearchManagerTest {

    @Test
    public void searchTest() {
        SearchManager searchManager = SearchManager.getInstance();
        searchManager.setSearchListener(new SearchManager.SearchListener() {
            @Override
            public void onSearchStarted(String keyword) {
                Ami.log("start search keyword: " + keyword);
            }

            @Override
            public void onSearchChanged(String keyword, @Nullable ObjectInfo newObjectInfo, @NonNull ArrayList<ObjectInfo> searchRst) {
                Ami.log("onSearchChanged --> " + newObjectInfo);
                Ami.log("onSearchChanged --> " + searchRst);
            }

            @Override
            public void onSearchFinished(String keyword, @NonNull ArrayList<ObjectInfo> searchRst) {
                Ami.log("onSearchFinished --> " + searchRst);
            }

            @Override
            public void onSearchCanceled() {

            }
        });
        searchManager.setSearchTarget(new Application());
        searchManager.searchKeyword("mDataDir");


        //等待异步进程结束
        SystemClock.sleep(3000);
    }
}
