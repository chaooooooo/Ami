package chao.app.ami.launcher.drawer;

import java.util.ArrayList;

public class DrawerGroup extends DrawerNode {
    private ArrayList<DrawerNode> mChildren = new ArrayList<>();

    public void addNode(DrawerNode node) {
        mChildren.add(node);
    }

    public void remove(DrawerNode node) {
        mChildren.remove(node);
    }

    public void clear() {
        mChildren.clear();
    }

    public int size() {
        return mChildren.size();
    }

    public DrawerNode getChild(int index) {
        return mChildren.get(index);
    }
}
