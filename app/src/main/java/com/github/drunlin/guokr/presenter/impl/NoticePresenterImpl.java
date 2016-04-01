package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.NoticeNum;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.model.NoticeModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.presenter.NoticePresenter;
import com.github.drunlin.guokr.view.NoticeView;

import javax.inject.Inject;

import static com.github.drunlin.guokr.util.JavaUtil.call;

/**
 * @author drunlin@outlook.com
 */
public class NoticePresenterImpl extends PresenterBase<NoticeView> implements NoticePresenter {
    @Inject NoticeModel noticeModel;
    @Inject UserModel userModel;

    @Override
    public void onViewCreated(boolean firstCreated) {
        bind(noticeModel.noticeCountResulted(), this::onNoticeCountResult);
        bind(userModel.loginStateChanged(), this::onLoggedStateChange);

        if (firstCreated) {
            onLoggedStateChange(userModel.isLoggedIn());
        } else {
            if (userModel.isLoggedIn()) {
                view.setLoginStatus(true);

                call(noticeModel.getNoticeNum(), this::setNoticeNum);
            } else {
                view.setLoginStatus(false);
            }
        }
    }

    private void onLoggedStateChange(boolean isLogged) {
        if (isLogged) {
            noticeModel.startServer();
        } else {
            noticeModel.stopServer();
        }

        view.setLoginStatus(isLogged);
    }

    private void onNoticeCountResult(int resultCode, NoticeNum num) {
        switch (resultCode) {
            case ResponseCode.OK:
                setNoticeNum(num);
                break;
            case ResponseCode.TOKEN_INVALID:
                userModel.logout();
                break;
        }
    }

    private void setNoticeNum(NoticeNum num) {
        view.setMessagesCount(num.message);
        view.setNoticesCount(num.notice);
        view.setAllNoticesCount(num.message + num.notice);
    }

    @Override
    public void onViewNotices() {
        NoticeNum num = noticeModel.getNoticeNum();
        if (num.notice > 0) {
            view.viewNotices();
        } else if (num.message > 0) {
            view.viewMessages();
        }
    }

    @Override
    public void onViewDestroyed() {
        noticeModel.stopServer();
    }
}
