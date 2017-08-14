package chao.app.ami.frames;

/**
 * @author chao.qin
 * @since 2017/8/13
 */

public interface IFrame {
    int getSize();

    Entry getEntry(int position);

    String getName();

    class Entry {
        Object object;
        String title;
        String value;

        Entry(String title, String value, Object object) {
            this.title = title;
            this.value = value;
            this.object = object;
        }
    }
}
