package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.bean.Notice;
import com.github.drunlin.guokr.bean.NoticeNum;
import com.github.drunlin.signals.impl.Signal1;
import com.github.drunlin.signals.impl.Signal2;

import java.util.List;

/**
 * 获取通知信息。
 *
 * @author drunlin@outlook.com
 */
public interface NoticeModel {
    /**
     * 启动服务定时请求通知。
     */
    void startServer();

    /**
     * 关闭通知服务器。
     */
    void stopServer();

    /**
     * 请求通知数目。
     */
    void requestNoticesCount();

    /**
     * 通知数目的结果。
     * @return [resultCode, noticesCount]
     */
    Signal2<Integer, NoticeNum> noticeCountResulted();

    /**
     * 获取最新的通知数目。
     * @return
     */
    NoticeNum getNoticeNum();

    /**
     * 请求通知数据。
     */
    void requestNotices();

    /**
     * 通知列表加载完成。
     * @return [resultCode, notices]
     */
    Signal2<Integer, List<Notice>> noticesResulted();

    /**
     * 加载完成的通知。
     * @return
     */
    List<Notice> getNotices();

    /**
     * 忽略所有的通知。
     */
    void ignoreAllNotices();

    /**
     * 忽略所有通知的结果。
     * @return [resultCode]
     */
    Signal1<Integer> ignoreAllNoticesResulted();
}
