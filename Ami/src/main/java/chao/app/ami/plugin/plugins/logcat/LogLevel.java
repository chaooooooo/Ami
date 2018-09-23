package chao.app.ami.plugin.plugins.logcat;

import android.content.res.ColorStateList;
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

    public ColorStateList getColor() {
        int pressed = 0x77000000 | ( 0x00ffffff & color);
        int selected = 0x77000000 | ( 0x00ffffff & color);
        int checked = 0x77000000 | ( 0x00ffffff & color);
        int[] colors = new int[] { pressed, selected, checked, color};
        int[][] states = new int[4][];
        states[0] = new int[]{ android.R.attr.state_pressed };
        states[1] = new int[]{ android.R.attr.state_selected };
        states[2] = new int[]{ android.R.attr.state_checked };
        states[3] = new int[]{};
        return new ColorStateList(states, colors);
    }
}
