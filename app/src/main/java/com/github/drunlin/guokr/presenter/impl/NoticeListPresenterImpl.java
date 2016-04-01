package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.Notice;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.model.NoticeModel;
import com.github.drunlin.guokr.presenter.NoticeListPresenter;
import com.github.drunlin.guokr.view.NoticeListView;

import java.util.List;

import javax.inject.Inject;

public class NoticeListPresenterImpl
        extends LoginNeededPresenterBase<NoticeListView> implements NoticeListPresenter {

    @Inject NoticeModel noticeModel;

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        bind(noticeModel.noticesResulted(), this::onNoticeListResult);
        bind(noticeModel.ignoreAllNoticesResulted(), this::onIgnoreAllResult);

        List<Notice> notices = noticeModel.getNotices();
        if (firstCreated || notices == null) {
            refresh();
        } else {
            view.setNotices(notices);
        }
    }

    @Override
    public void refresh() {
        noticeModel.requestNotices();

        view.setLoading(true);
    }

    private void onNoticeListResult(int resultCode, List<Notice> notices) {
        if (resultCode == ResponseCode.OK) {
            view.setNotices(notices);
        } else {
            view.onLoadFailed();
        }
        view.setLoading(false);
    }

    @Override
    public void ignoreAll() {
        noticeModel.ignoreAllNotices();
    }

    private void onIgnoreAllResult(int resultCode) {
        if (resultCode == ResponseCode.OK) {
            view.setNotices(noticeModel.getNotices());
        } else {
            view.onIgnoreAllFailed();
        }
    }

    @Override
    public void onViewNotice(Notice notice) {
        view.viewNotice(String.format("http://m.guokr.com/user/notice/%d/", notice.id));
    }

    @Override
    public void onViewNotices() {
        view.openInBrowser("http://m.guokr.com/user/notice/");
    }
}
