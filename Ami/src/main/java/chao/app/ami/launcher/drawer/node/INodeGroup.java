package chao.app.ami.launcher.drawer.node;

/**
 * @author chao.qin
 * @since 2017/8/1
 */

interface INodeGroup {
    void addNode(Node node);

    void remove(Node node);

    void clear();

    int size();

    Node getChild(int index);
}
