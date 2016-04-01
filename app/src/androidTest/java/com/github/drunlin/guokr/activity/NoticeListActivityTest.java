package com.github.drunlin.guokr.activity;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Notice;
import com.github.drunlin.guokr.bean.ResultClassMap.NoticesResult;
import com.github.drunlin.guokr.test.ActivityTestCase;

import java.util.List;

import static com.github.drunlin.guokr.test.util.TestUtils.getResult;

/**
 * @author drunlin@outlook.com
 */
public class NoticeListActivityTest extends ActivityTestCase<NoticeListActivity> {
    public NoticeListActivityTest() {
        super(NoticeListActivity.class);
    }

    @Override
    protected void onPreview() throws Throwable {
        List<Notice> notices = getResult(NoticesResult.class, R.raw.notice_list);
        for (Notice notice : notices) {
            notice.dateLastUpdated = "2015-01-01";
        }
        runOnUiThread(() -> activity.setNotices(notices));
    }
}
