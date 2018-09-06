package chao.app.ami.plugin.plugins.logcat;

import android.graphics.Color;

/**
 * @author qinchao
 * @since 2018/9/5
 */
public enum LogLevel {
    VERBOSE("V", Color.WHITE),
    DEBUG("D", Color.BLUE),
    INFO("I", Color.CYAN),
    WARNING("W", Color.YELLOW),
    ERROR("E", Color.RED),
    ASSERT("A", Color.RED),
    UNKNOWN("", Color.WHITE);

    private String tag;

    private int color;

    LogLevel(String tag, int color) {
        this.tag = tag;
        this.color = color;
    }
    public static LogLevel indexOf(String tag) {
        for(LogLevel level: values()) {
            if (level.tag.equalsIgnoreCase(tag)) {
                return level;
            }
        }
        return UNKNOWN;
    }

    /**
     * 小于等于
     *
     * @param level settings.level
     */
    public boolean gte(LogLevel level) {
        return ordinal() >= level.ordinal();
    }

    public String getTag() {
        return tag;
    }

    public int getColor() {
        return color;
    }
}
