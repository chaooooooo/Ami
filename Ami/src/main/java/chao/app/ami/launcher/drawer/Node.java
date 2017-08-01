package chao.app.ami.launcher.drawer;

import java.util.ArrayList;

/**
 * @author chao.qin
 * @since 2017/8/1
 */

public class Node implements Constants{

    private ArrayList<Property> mProperties = new ArrayList<>();

    private String mName;

    private NodeGroup mParent;

    public Node(){

    }

    public Node(String name) {
        mName = name;
    }

    public void setParent(NodeGroup node) {
        mParent = node;
        mParent.addNode(this);
    }

    public NodeGroup getParent() {
        return mParent;
    }

    public String getName() {
        return mName;
    }

    public void addProperty(Property property) {
        mProperties.add(property);
    }
}
