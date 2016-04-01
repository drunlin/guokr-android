package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.Message;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.model.MessageModel;
import com.github.drunlin.guokr.presenter.MessageListPresenter;
import com.github.drunlin.guokr.view.MessageListView;

import java.util.List;

import javax.inject.Inject;

/**
 * 站内信列表，用WebView显示信的内容。
 *
 * @author drunlin@outlook.com
 */
public class MessageListPresenterImpl
        extends LoginNeededPresenterBase<MessageListView> implements MessageListPresenter {

    @Inject MessageModel messageModel;

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        bind(messageModel.messagesResulted(), this::onMessageResult);

        List<Message> messages = messageModel.getMassages();
        if (firstCreated || messages == null) {
            refresh();
        } else {
            view.setMassages(messages);
        }
    }

    @Override
    public void refresh() {
        messageModel.requestMessages();

        view.setLoading(true);
    }

    private void onMessageResult(int resultCode, boolean isRefresh, List<Message> messages) {
        if (resultCode == ResponseCode.OK) {
            if (isRefresh) {
                view.setMassages(messages);
            } else {
                view.onMassagesAppended();
            }
        } else {
            view.onLoadMessagesFailed();
        }
        view.setLoading(false);
    }

    @Override
    public void loadMore() {
        messageModel.requestMoreMessages();
    }

    @Override
    public void onViewMessage(Message message) {
        view.viewMessage(messageModel.getMessageUrl(message));
    }

    @Override
    public void onViewMessages() {
        view.viewMessages("http://m.guokr.com/user/messages/");
    }
}
