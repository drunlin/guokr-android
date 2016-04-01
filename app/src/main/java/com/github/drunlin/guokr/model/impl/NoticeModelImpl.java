package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.bean.Notice;
import com.github.drunlin.guokr.bean.NoticeNum;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.bean.ResultClassMap.NoticeNumResult;
import com.github.drunlin.guokr.bean.ResultClassMap.NoticesResult;
import com.github.drunlin.guokr.model.Model;
import com.github.drunlin.guokr.model.NetworkModel;
import com.github.drunlin.guokr.model.NoticeModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.model.request.AccessRequest;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.signals.impl.Signal1;
import com.github.drunlin.signals.impl.Signal2;
import com.google.common.annotations.VisibleForTesting;

import java.util.List;

import javax.inject.Inject;

import static com.android.volley.Request.Method.PUT;
import static com.github.drunlin.guokr.util.JavaUtil.foreach;

/**
 * 定时请求通知信息来实现通知功能。
 *
 * @author drunlin@outlook.com
 */
public class NoticeModelImpl extends Model implements NoticeModel {
    private final Signal2<Integer, NoticeNum> noticeCountResulted = new Signal2<>();
    private final Signal2<Integer, List<Notice>> noticesResulted = new Signal2<>();
    private final Signal1<Integer> ignoreAllNoticesResulted = new Signal1<>();

    @Inject NetworkModel networkModel;
    @Inject UserModel userModel;

    private Thread noticeThread;

    private NoticeNum noticeNum;

    private List<Notice> notices;

    public NoticeModelImpl(Injector injector) {
        super(injector);
    }

    @Override
    public void requestNoticesCount() {
        final String url = "http://www.guokr.com/apis/community/rn_num.json";
        new AccessRequest.Builder<>(url, NoticeNumResult.class, userModel.getToken())
                .addParam("_", System.currentTimeMillis())
                .setListener(this::onNoticeCountResult)
                .setErrorListener(e -> noticeCountResulted.dispatch(e.getCode(), null))
                .build(networkModel);
    }

    private void onNoticeCountResult(NoticeNumResult result) {
        noticeNum = result.result;

        noticeCountResulted.dispatch(ResponseCode.OK, noticeNum);
    }

    @Override
    public Signal2<Integer, NoticeNum> noticeCountResulted() {
        return noticeCountResulted;
    }

    @Override
    public NoticeNum getNoticeNum() {
        return noticeNum;
    }

    @Override
    public void startServer() {
        startServer(3_000);
    }

    @VisibleForTesting
    void startServer(long duration) {
        if (noticeThread != null) {
            return;
        }
        noticeThread = new Thread(() -> {
            while (true) {
                requestNoticesCount();
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        noticeThread.start();
    }

    @Override
    public void stopServer() {
        if (noticeThread != null) {
            noticeThread.interrupt();
            noticeThread = null;
        }
    }

    @Override
    public void requestNotices() {
        final String url = "http://www.guokr.com/apis/community/notice.json";
        new AccessRequest.Builder<>(url, NoticesResult.class, userModel.getToken())
                .addParam("_", System.currentTimeMillis())
                .addParam("limit", 100)
                .setListener(r -> noticesResulted.dispatch(ResponseCode.OK, notices = r.result))
                .setErrorListener(e -> noticesResulted.dispatch(e.getCode(), null))
                .build(networkModel);
    }

    @Override
    public List<Notice> getNotices() {
        return notices;
    }

    @Override
    public Signal2<Integer, List<Notice>> noticesResulted() {
        return noticesResulted;
    }

    @Override
    public void ignoreAllNotices() {
        final String url = "http://www.guokr.com/apis/community/notice_ignore.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(PUT)
                .setListener(r -> onIgnoreAllNoticesResult())
                .setErrorListener(e -> ignoreAllNoticesResulted.dispatch(e.getCode()))
                .build(networkModel);
    }

    private void onIgnoreAllNoticesResult() {
        foreach(notices, notice -> notice.isRead = true);

        ignoreAllNoticesResulted.dispatch(ResponseCode.OK);
    }

    @Override
    public Signal1<Integer> ignoreAllNoticesResulted() {
        return ignoreAllNoticesResulted;
    }
}
