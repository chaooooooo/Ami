package chao.app.ami.launcher.drawer;

/**
 * @author chao.qin
 * @since 2017/8/1
 */

public class Extra extends Property {
    private String mFormat;

    public Extra(String key, String value, String format) {
        super(key, value);
        mFormat = format;
    }

    public String getFormat() {
        return mFormat;
    }

    public void setFormat(String format) {
        mFormat = format;
    }
}
