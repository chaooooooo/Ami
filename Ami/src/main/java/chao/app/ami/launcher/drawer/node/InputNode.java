package chao.app.ami.launcher.drawer.node;

/**
 * @author chao.qin
 * @since 2017/8/1
 */

public class InputNode extends Node {

    private String mViewId;

    private String mText;

    public InputNode(String viewId, String text) {
        mViewId = viewId;
        mText = text;
    }

    public String getViewId(){
        return mViewId;
    }

    public String getText() {
        return mText;
    }
}
