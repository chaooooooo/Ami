package chao.app.ami.launcher.drawer.node;

/**
 * @author chao.qin
 * @since 2017/8/1
 */

public class Property extends Node {

    private String key;
    private String value;
    private String format;

    public Property(String key, String value, String format) {
        super(key);
        this.key = key;
        this.value = value;
        this.format = format;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
