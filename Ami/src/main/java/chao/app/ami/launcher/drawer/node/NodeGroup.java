package chao.app.ami.launcher.drawer.node;

import android.text.TextUtils;
import java.io.Serializable;
import java.util.List;

/**
 * @author chao.qin
 * @since 2017/8/1
 */

public class NodeGroup extends Node implements Serializable {

    private String mPackageName;

    public NodeGroup(String name) {
        super(name);
    }

    public Node findNodeByName(String name) {
        List<Node> children = getChildren();
        for (Node child: children) {
            if (child.getName().equals(name)) {
                return child;
            }
            if (child instanceof NodeGroup) {
                Node r = ((NodeGroup) child).findNodeByName(name);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }

    public ComponentNode findNodeByComponent(String component) {
        if (TextUtils.isEmpty(component)) {
            return null;
        }
        List<Node> children = getChildren();
        for (Node child: children) {
            if (child instanceof ComponentNode) {
                ComponentNode componentNode = (ComponentNode) child;
                if(component.equals(componentNode.getComponent())) {
                    return componentNode;
                }
            }
            if (child instanceof NodeGroup) {
                ComponentNode r = ((NodeGroup) child).findNodeByComponent(component);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }

    @Override
    public void addNode(Node node) {
        super.addNode(node);
        if (node instanceof ComponentNode) {
            ((ComponentNode) node).setPkgName(mPackageName);
        }
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }

}
