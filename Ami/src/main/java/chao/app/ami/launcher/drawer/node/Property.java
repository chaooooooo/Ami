package chao.app.ami.launcher.drawer.node;

/**
 * @author chao.qin
 * @since 2017/8/1
 */

public class Property extends Node {

    private String mKey;
    private String mValue;

    public Property(String key, String value) {
        super(key);
        mKey = key;
        mValue = value;
    }

    public String getKey() {
        return mKey;
    }

    public String getValue() {
        return mValue;
    }
}
