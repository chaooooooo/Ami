package chao.app.ami.plugin.plugins.frame;

/**
 * @author chao.qin
 * @since 2017/8/13
 */

public interface IFrame {
    int getSize();

    Entry getEntry(int position);

    CharSequence getName();

    Object getSource();


    class Entry {
        Object value;
        CharSequence title;
        CharSequence className;
        boolean isCategory;

        public Entry(CharSequence className, boolean isCategory) {
            this.className = className;
            this.isCategory = isCategory;

        }

        public Entry(Object value, CharSequence title, CharSequence className) {
            this.value = value;
            this.title = title;
            this.className = className;
            this.isCategory = false;
        }

        public CharSequence getTitle() {
            return title;
        }

        public void setTitle(CharSequence title) {
            this.title = title;
        }

        public CharSequence getClassName() {
            return className;
        }

        public void setClassName(CharSequence className) {
            this.className = className;
        }

        public boolean isCategory() {
            return isCategory;
        }

        public void setCategory(boolean category) {
            isCategory = category;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

}
