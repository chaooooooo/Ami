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

    String RES_TYPE_ATTR = "attr";
    String RES_TYPE_DRAWABLE = "drawable";
    String RES_TYPE_MIPMAP = "mipmap";
    String RES_TYPE_LAYOUT = "layout";
    String RES_TYPE_ANIM = "anim";
    String RES_TYPE_RAW = "raw";
    String RES_TYPE_STRING = "string";
    String RES_TYPE_DIMEN = "dimen";
    String RES_TYPE_STYLE = "style";
    String RES_TYPE_BOOL = "bool";
    String RES_TYPE_COLOR = "color";
    String RES_TYPE_ID = "id";
    String RES_TYPE_INTEGER = "integer";

    String[] RES_TYPES = new String[] {
            RES_TYPE_ATTR,
            RES_TYPE_DRAWABLE,
            RES_TYPE_MIPMAP,
            RES_TYPE_LAYOUT,
            RES_TYPE_ANIM,
            RES_TYPE_RAW,
            RES_TYPE_STRING,
            RES_TYPE_DIMEN,
            RES_TYPE_STRING,
            RES_TYPE_STYLE,
            RES_TYPE_BOOL,
            RES_TYPE_COLOR,
            RES_TYPE_ID,
            RES_TYPE_INTEGER
    };
}
