package chao.app.ami.launcher.drawer.node;

import chao.app.ami.launcher.drawer.node.NodeGroup;

/**
 * @author chao.qin
 * @since 2017/8/1
 */

public class DrawerNode extends NodeGroup {

    private String mPackageName;

    public DrawerNode(String name) {
        super(name);
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }
}
