package chao.app.debug.launcher.drawer;

public class DrawerNode {

    private DrawerGroup mParent;
    private String mNodeName;
    private String mComponent;

    public void setParent(DrawerGroup node) {
        mParent = node;
        mParent.addNode(this);
    }

    public DrawerGroup getParent() {
        return mParent;
    }

    public void setNodeName(String name) {
        mNodeName = name;
    }

    public String getNodeName() {
        return mNodeName;
    }

    public void setComponent(String component) {
        mComponent = component;
    }

    public String getComponent() {
        return mComponent;
    }

}
