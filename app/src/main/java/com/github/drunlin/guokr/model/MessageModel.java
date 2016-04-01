package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.bean.Message;
import com.github.drunlin.signals.impl.Signal3;

import java.util.List;

/**
 * 站内信数据操作。
 *
 * @author drunlin@outlook.com
 */
public interface MessageModel {
    /**
     * 请求站内信，会清除之前的结果。
     */
    void requestMessages();

    /**
     * 请求更多的数据。
     */
    void requestMoreMessages();

    /**
     * 列表加载完成。
     * @return [resultCode, isRefresh, messages]
     */
    Signal3<Integer, Boolean, List<Message>> messagesResulted();

    /**
     * 加载完成的站内信。
     * @return
     */
    List<Message> getMassages();

    /**
     * 获取信对应的URL。
     * @param message
     * @return
     */
    String getMessageUrl(Message message);
}
