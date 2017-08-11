package chao.app.ami;

import chao.app.ami.utils.DeviceUtil;

/**
 * @author chao.qin
 * @since 2017/8/2
 */

public interface Constants {
    String OBJECT_UNKNOWN = "unknown";
    String OBJECT_NULL = "null";

    String CATEGORY = "category";

    String AMI_ACTION_LONG_CLICK = "长按事件";
    String AMI_ACTION_VIEW_DETAIL = "显示View详细信息";

    int MAX_LIST_WIDTH = DeviceUtil.dp2px(200);
    int MAX_LIST_HEIGHT = DeviceUtil.dp2px(360);
}
