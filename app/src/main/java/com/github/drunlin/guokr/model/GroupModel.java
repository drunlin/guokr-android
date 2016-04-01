package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.bean.Group;
import com.github.drunlin.signals.impl.Signal1;
import com.github.drunlin.signals.impl.Signal2;

/**
 * 小组相关的网络操作。
 * @see ForumModel
 *
 * @author drunlin@outlook.com
 */
public interface GroupModel {
    /**
     * 请求小组的数据。
     */
    void requestGroup();

    /**
     * 小组数据加载完成。
     * @return [resultCode, groups]
     */
    Signal2<Integer, Group> groupResulted();

    /**
     * 获取小组信息。
     * @return
     */
    Group getGroup();

    /**
     * 加入当前小组。
     */
    void joinGroup();

    /**
     * 请求加入小组的结果。
     * @return [resultCode]
     */
    Signal1<Integer> joinGroupResulted();

    /**
     * 退出当前小组。
     */
    void quitGroup();

    /**
     * 请求退出小组的结果。
     * @return [resultCode]
     */
    Signal1<Integer> quitGroupResulted();
}
