package chao.app.ami.text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author chao.qin
 * @since 2017/7/29
 */

class PoetryReader {

    private static final int INDEX_LENGTH = 3;  //诗歌的索引长度只能为3 如011，012，013...
    private static final int INVALID_INDEX = -1;

    private Scanner mPoetryScanner;

    private ArrayList<Poetry> mPoetryList = new ArrayList<>();

    public PoetryReader() {
    }

    public ArrayList<Poetry> readPoetry(InputStream is) {
        mPoetryScanner = new Scanner(is);
        Poetry currentPoetry = null;
        String currentAuthor = null;
        while (mPoetryScanner.hasNext()) {
            String line = mPoetryScanner.nextLine().trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.startsWith("#")) {
                continue;
            }
            int index = poetryIndex(line);
            if (index != INVALID_INDEX) {
                Poetry poetry = new Poetry();
                poetry.setIndex(index);
                String[] splits = line.split(":",2);
                if (splits.length == 2) {
                    currentAuthor = splits[0].substring(3, splits[0].length());
                    poetry.setPoetryName(splits[1]);
                } else {
                    poetry.setPoetryName(splits[0].substring(3, splits[0].length()));
                }
                poetry.setAuthor(currentAuthor);
                currentPoetry = poetry;
                mPoetryList.add(poetry);
            } else if (currentPoetry != null){
                currentPoetry.addLine(line);
            }
        }
        mPoetryScanner.close();
        return mPoetryList;
    }

    private int poetryIndex(String line) {
        if (line == null) {
            return INVALID_INDEX;
        }
        line = line.trim();
        if (line.length() < INDEX_LENGTH) {
            return INVALID_INDEX;
        }
        char[] chars = line.substring(0, INDEX_LENGTH).toCharArray();
        if (chars.length != INDEX_LENGTH) {
            throw new IllegalStateException("err line length");
        }
        for (int i = 0; i < INDEX_LENGTH; i++) {
            if (chars[i] < '0' || chars[i] > '9') {
                return INVALID_INDEX;
            }
        }
        int index = 0;
        for (char ch: chars) {
            index = index * 10 + (ch - '0');
        }
        return index;
    }
}
