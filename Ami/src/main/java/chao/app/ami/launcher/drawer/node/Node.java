package chao.app.ami.launcher.drawer.node;

import chao.app.ami.launcher.drawer.DrawerConstants;
import java.util.ArrayList;

/**
 * @author chao.qin
 * @since 2017/8/1
 */

public class Node implements DrawerConstants {

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
        mChildren.add(node);
    }

    public void remove(Node node) {
        mChildren.remove(node);
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

    public ArrayList<Node> getmChildren() {
        return mChildren;
    }
}
