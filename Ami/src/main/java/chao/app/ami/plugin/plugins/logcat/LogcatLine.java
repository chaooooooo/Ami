package chao.app.ami.plugin.plugins.logcat;

/**
 * @author qinchao
 * @since 2018/9/5
 */
public class LogcatLine {

    private String date = "";

    private String time = "";

    private String log;

    private String pid = "-1";

    private String tid = "-1";

    private LogLevel level;

    private String tag;

    public LogcatLine(String originLine) {
        String[] splits = originLine.split("\\s+");
        if (splits.length < 7) {
            log = originLine;
            level = LogLevel.UNKNOWN;
            return;
        }

        date = splits[0];
        time = splits[1];
        pid = splits[2];
        tid = splits[3];
        level = LogLevel.indexOf(splits[4]);
        tag = splits[5].substring(0, splits[5].length());
        StringBuilder buffer = new StringBuilder();
        for (int i = 6; i < splits.length; i++) {
            buffer.append(splits[i]);
            buffer.append(" ");
        }
        log = level.getTag() + ": " + tag + ": " + buffer.toString();
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLog() {
        return log;
    }

    public String getPid() {
        return pid;
    }

    public String getTid() {
        return tid;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getTag() {
        return tag;
    }
}
