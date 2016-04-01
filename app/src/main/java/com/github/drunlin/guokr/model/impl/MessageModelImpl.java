package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.bean.Message;
import com.github.drunlin.guokr.bean.MessagesResult;
import com.github.drunlin.guokr.bean.UserInfo;
import com.github.drunlin.guokr.model.ListModeBase;
import com.github.drunlin.guokr.model.MessageModel;
import com.github.drunlin.guokr.model.NetworkModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.model.request.AccessRequest;
import com.github.drunlin.guokr.model.request.HttpRequest.RequestError;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.signals.impl.Signal3;

import java.util.List;

import javax.inject.Inject;

/**
 * 站内信相关的数据。
 *
 * @author drunlin@outlook.com
 */
public class MessageModelImpl
        extends ListModeBase<Message, MessagesResult, RequestError> implements MessageModel {

    @Inject NetworkModel networkModel;
    @Inject UserModel userModel;

    public MessageModelImpl(Injector injector) {
        super(injector);
    }

    @Override
    protected void onRequest() {
        final String url = "http://www.guokr.com/apis/community/user/message.json";
        new AccessRequest.Builder<>(url, MessagesResult.class, userModel.getToken())
                .addParam("limit", 20)
                .addParam("offset", offset)
                .setParseListener(this::onParseResult)
                .setListener(response -> onDeliverResult(response.result.recentList))
                .setErrorListener(this::onDeliverError)
                .build(networkModel);
    }

    @Override
    protected void onParseResult(MessagesResult response) {
        for (Message message : response.result.recentList) {
            UserInfo sender = userModel.getUserInfo(message.authorKey);
            message.another = sender != null ? sender : new UserInfo();
        }
    }

    @Override
    protected void onDeliverError(RequestError error) {
        super.onDeliverError(error);

        resulted.dispatch(error.getCode(), false, null);
    }

    @Override
    public void requestMessages() {
        refresh();
    }

    @Override
    public void requestMoreMessages() {
        requestMore();
    }

    @Override
    public Signal3<Integer, Boolean, List<Message>> messagesResulted() {
        return resulted;
    }

    @Override
    public List<Message> getMassages() {
        return result;
    }

    @Override
    public String getMessageUrl(Message message) {
        String userId = keyToId(message.authorKey);
        return String.format("http://m.guokr.com/user/messages/%s/", userId);
    }

    private String keyToId(String key) {
        String id = Long.valueOf(key, 36).toString();
        StringBuilder builder = new StringBuilder(10);
        for (int i = 10 - id.length(); i > 0; --i) {
            builder.append('0');
        }
        builder.append(id);
        return builder.toString();
    }
}
