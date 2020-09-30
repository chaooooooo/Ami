package chao.app.ami.text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import chao.app.ami.Ami;
import chao.app.ami.utils.Util;

/**
 * @author chao.qin
 * @since 2017/7/29
 *
 *
 *
 * 后期优化内存
 * 后期扩展更多内容： 扩充诗词库， 添加段子等。
 *
 */

public class TextManager {

    private static ArrayList<Poetry> sPoetryList;
    private static ArrayList<Poetry> sSongPoetryList;

    private TextManager() {
    }

    public static void init() {
        try {
            sPoetryList = readPoetry("text/poetry.txt");
            sSongPoetryList = readPoetry("text/song_poetry.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Poetry> readPoetry(String poetryPath) throws IOException {
        InputStream is = null;
        try {
            is = Ami.getApp().getAssets().open(poetryPath);
            PoetryReader reader = new PoetryReader();
            return reader.readPoetry(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     *  随机获取一首唐诗
     */
    public static Poetry getTPoetry() {
        checkDebugMode();
        if (sPoetryList == null) {
            init();
        }
        int size = sPoetryList.size();
        int rand = (int) (Math.random() * size);
        return sPoetryList.get(rand);
    }

    /**
     * 随机获取 size首唐诗
     *
     */
    public static Poetry[] getTPoetry(int size) {
        checkDebugMode();
        Poetry[] poetries = new Poetry[size];
        for (int i=0;i<size;i++) {
            poetries[i] = getTPoetry();
        }
        return poetries;

    }

    /**
     * 随机获取一首宋词
     *
     */
    public static Poetry getSPoetry() {
        checkDebugMode();
        if (sSongPoetryList == null) {
            init();
        }
        int size = sSongPoetryList.size();
        int rand = (int) (Math.random() * size);
        return sSongPoetryList.get(rand);
    }

    /**
     * 随机获取 size首宋词
     *
     */
    public static Poetry[] getSPoetry(int size) {
        checkDebugMode();
        Poetry[] poetries = new Poetry[size];
        for (int i=0;i<size;i++) {
            poetries[i] = getSPoetry();
        }
        return poetries;

    }

    private static void checkDebugMode() {
        if (!Util.isHostAppDebugMode(Ami.getApp())) {
//            throw new IllegalStateException("TextManager must be use in debug mode.");
        }
    }
}
