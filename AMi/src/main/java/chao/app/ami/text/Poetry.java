package chao.app.ami.text;

import java.util.ArrayList;

/**
 * @author chao.qin
 * @since 2017/7/29
 *
 * 唐诗 dynasty
 *
 * 作者
 * 诗歌名
 * 诗歌内容
 *
 * 宋词
 *
 */

public class Poetry {
    private String mPoetryName;
    private String mAuthor;
    private ArrayList<String> mPoetryLines = new ArrayList<>();
    private int mIndex;

    public String getPoetryName() {
        return mPoetryName;
    }

    public void setPoetryName(String poetryName) {
        this.mPoetryName = poetryName;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public ArrayList<String> getContent() {
        return mPoetryLines;
    }

    public void addLine(String line) {
        mPoetryLines.add(line);
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }
}
