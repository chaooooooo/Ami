package chao.app.ami.launcher.drawer.node;

import java.util.ArrayList;

/**
 * @author chao.qin
 * @since 2017/8/1
 */

public class NodeGroup extends Node implements INodeGroup {

    public NodeGroup(String name) {
        super(name);
    }

    private ArrayList<Node> mChildren = new ArrayList<>();

    @Override
    public void addNode(Node node) {
        mChildren.add(node);
    }

    @Override
    public void remove(Node node) {
        mChildren.remove(node);
    }

    @Override
    public void clear() {
        mChildren.clear();
    }

    @Override
    public int size() {
        return mChildren.size();
    }

    @Override
    public Node getChild(int index) {
        return mChildren.get(index);
    }
}
