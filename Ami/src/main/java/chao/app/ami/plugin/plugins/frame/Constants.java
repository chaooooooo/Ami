package chao.app.ami.plugin.plugins.frame;

/**
 * @author chao.qin
 * @since 2018/8/10
 */

public interface Constants {
    //================ 搜索相关 ============================//
    /**
     *  字段搜索的时间间隔
     */
    int SEARCH_KEYWORD_INTERVAL = 1500; //1500ms


    /**
     *  最小搜索长度
     */
    int MIN_SEARCH_LENGTH = 3;

    /**
     *  为了防止类之间存在循环引用的情况，限制相同类型相互搜索
     */
    int MAX_SAME_COUNT = 2;

    /**
     *  搜索最大深度
     *
     *  如果不限制深度，搜索时间会无限长
     *  MAX_DEEP值越大， 搜索花费时间将越长
     */
    int MAX_DEEP = 5;



}
