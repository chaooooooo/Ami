package chao.app.ami.plugin.plugins.logcat;

import chao.app.ami.plugin.AmiSettings;

/**
 * @author qinchao
 * @since 2018/9/6
 */
public class LogcatSettings extends AmiSettings {

    private static final int DEFAULT_CACHE_SIZE = 500;

    /**
     * logcat文字大小缩放
     */
    private int zoom = 0;

    /**
     * logcat缓存行数
     */
    private int cacheSize = DEFAULT_CACHE_SIZE;

    /**
     *
     *  心跳间隔，单位ms
     *  间隔<=0表示关闭心跳
     */
    private boolean heart = false;

    /**
     * 暂停，恢复
     */
    private boolean pause = false;

    /**
     * 日志等级
     */
    private LogLevel level = LogLevel.VERBOSE;

    public int getZoom() {
        return zoom;
    }

    public void zoomIn() {
        this.zoom++;
    }

    public void zoomOut() {
        this.zoom--;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public boolean isHeart() {
        return heart;
    }

    public void setHeart(boolean heart) {
        this.heart = heart;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }
}
