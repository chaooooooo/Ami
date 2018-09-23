package chao.app.ami.plugin.plugins.frame;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;

import chao.app.ami.plugin.plugins.frame.search.ObjectInfo;

/**
 * @author chao.qin
 * @since 2018/8/8
 */

public class SearchFrame extends FrameImpl {

    private ArrayList<ObjectInfo> mSearchRst;

    private String mSearchText;

    public SearchFrame(Object searchTarget) {
        super(searchTarget);
    }

    @Override
    public int getSize() {
        return mSearchRst.size();
    }

    @Override
    public SearchEntry getEntry(int position) {
        ObjectInfo objectInfo = mSearchRst.get(position);
        CharSequence className = objectInfo.getClassName();
        CharSequence name = objectInfo.getName();
        Object value = objectInfo.getObject();

        SpannableStringBuilder pathBuilder = new SpannableStringBuilder();
        ObjectInfo parentObjectInfo = objectInfo.getParent();
        int deep = objectInfo.getDeep();
        while (parentObjectInfo != null) {
            SpannableStringBuilder itemSpan = new SpannableStringBuilder();
            for (int i = deep; i > 0; i--) {
                itemSpan.append(" ");
            }
            SpannableString fieldName = parentObjectInfo.getName();
            fieldName.setSpan(new ForegroundColorSpan(0xff424242), 0, fieldName.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            itemSpan.append("┕")
                    .append(parentObjectInfo.getClassName())
                    .append(".")
                    .append(parentObjectInfo.getName())
                    .append("\n");
            pathBuilder.insert(0, itemSpan);
            parentObjectInfo = parentObjectInfo.getParent();
            deep--;
        }
        return new SearchEntry(value, name, className, pathBuilder);
    }

    @Override
    public String getName() {
        return "#" + mSearchText;
    }

    public void setSearchRst(ArrayList<ObjectInfo> searchRst) {
        mSearchRst = searchRst;
    }

    public void setSearchText(String searchText) {
        mSearchText = searchText;
    }

    public class SearchEntry extends IFrame.Entry {

        public CharSequence searchPath;

        SearchEntry(Object value, CharSequence name, CharSequence className, CharSequence searchPath) {
            super(value, name, className);
            this.searchPath = searchPath;
        }

        public CharSequence getSearchPath() {
            return searchPath;
        }
    }
}
