package chao.app.ami.plugin.plugins.logcat;

import chao.app.ami.utils.Util;

/**
 * @author qinchao
 * @since 2018/9/23
 */
public class SearchParam {

    private String tag;

    private String tid;

    private LogLevel level;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public String searchTitle() {
        StringBuilder buffer = new StringBuilder();
        if (tag != null) {
            buffer.append("TAG:").append(tag);
        }
        if (tid != null) {
            if (buffer.length() > 0) {
                buffer.append(", ");
            }
            buffer.append("Thread:").append(Util.getThreadName(tid));
        }
        if (level != null) {
            if (buffer.length() > 0 && !(buffer.charAt(buffer.length() - 1) == ' ')) {
                buffer.append(", ");
            }
            buffer.append("Level:").append(level.name());
        }
        return buffer.toString();
    }

    public void reset() {
        level = null;
        tid = null;
        tag = null;
    }
}
