package chao.app.ami.launcher.drawer.node;

import chao.app.ami.launcher.drawer.DrawerConstants;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author chao.qin
 * @since 2017/8/1
 */

public class Node implements DrawerConstants, Serializable {

    private ArrayList<Property> mProperties = new ArrayList<>();

    private ArrayList<Node> mChildren = new ArrayList<>();

    private String mName;

    private Node mParent;


    public Node(){

    }

    public Node(String name) {
        mName = name;
    }

    public void setParent(Node node) {
        mParent = node;
        mParent.addNode(this);
    }

    public Node getParent() {
        return mParent;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public void addProperty(Property property) {
        mProperties.add(property);
    }

    public void addNode(Node node) {
        if (node.mParent != null && node.mParent != this) {
            throw new RuntimeException("node has already has a parent." + node.mParent);
        }
        mChildren.add(node);
        node.mParent = this;
    }

    public void remove(Node node) {
        mChildren.remove(node);
        node.mParent = null;
    }

    public void clear() {
        mChildren.clear();
    }

    public int size() {
        return mChildren.size();
    }

    public Node getChild(int index) {
        return mChildren.get(index);
    }

    public ArrayList<Node> getChildren() {
        return mChildren;
    }
}
