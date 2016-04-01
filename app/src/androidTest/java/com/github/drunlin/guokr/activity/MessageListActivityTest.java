package com.github.drunlin.guokr.activity;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Icon;
import com.github.drunlin.guokr.bean.Message;
import com.github.drunlin.guokr.bean.MessagesResult;
import com.github.drunlin.guokr.bean.UserInfo;
import com.github.drunlin.guokr.test.ActivityTestCase;

import java.util.List;

import static com.github.drunlin.guokr.test.util.TestUtils.getBean;
import static com.github.drunlin.guokr.test.util.TestUtils.getBitmapData;

/**
 * @author drunlin@outlook.com
 */
public class MessageListActivityTest extends ActivityTestCase<MessageListActivity> {
    public MessageListActivityTest() {
        super(MessageListActivity.class);
    }

    @Override
    protected void onPreview() throws Throwable {
        List<Message> messages =
                getBean(MessagesResult.class, R.raw.message_list).result.recentList;
        for (Message message : messages) {
            message.dateCreated = "2015-01-01";
            message.another = new UserInfo();
            message.another.nickname = "Name";
            message.another.avatar = new Icon();
            message.another.avatar.data = getBitmapData(R.drawable.ic_avatar);
        }
        runOnUiThread(() -> activity.setMassages(messages));
    }
}
