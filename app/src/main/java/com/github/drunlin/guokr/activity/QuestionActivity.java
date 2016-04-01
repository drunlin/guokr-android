package com.github.drunlin.guokr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.Intents;
import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Answer;
import com.github.drunlin.guokr.bean.QuestionContent;
import com.github.drunlin.guokr.fragment.RecommendDialogFragment;
import com.github.drunlin.guokr.presenter.QuestionPresenter;
import com.github.drunlin.guokr.presenter.QuestionPresenter.Factory;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.view.QuestionView;

import butterknife.Bind;

/**
 * 问题和其回答的界面，暂时没有评论回答的功能。
 *
 * @author drunlin@outlook.com
 */
public class QuestionActivity extends
        ContentActivity<QuestionContent, Answer, QuestionPresenter> implements QuestionView {

    @Bind(R.id.btn_recommend) FloatingActionButton recommendButton;

    public static Intent getIntent(int questionId) {
        Intent intent = new Intent(App.getContext(), QuestionActivity.class);
        intent.setAction(Intents.ACTION_VIEW_QUESTION);
        intent.putExtra(Intents.EXTRA_QUESTION_ID, questionId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recommendButton.setOnClickListener(v -> presenter.onRecommend());
    }

    @Override
    protected QuestionPresenter onCreatePresenter(String action, Intent intent) {
        int questionId = Intent.ACTION_VIEW.equals(action)
                ? Intents.getIdFromUri(intent, "question")
                : intent.getIntExtra(Intents.EXTRA_QUESTION_ID, 0);
        return lifecycle.bind(Factory.class, f -> f.create(questionId));
    }

    @Override
    protected int getFloatingMenuRes() {
        return R.layout.floating_menu_simple;
    }

    @Override
    public void recommend(String title, String summary, String url) {
        RecommendDialogFragment.show(getSupportFragmentManager(), title, summary, url);
    }

    @Override
    public void onLoadRepliesFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_load_answers_failed);
    }

    @Override
    public void onReplyFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_answer_failed);
    }

    @Override
    public void onDeleteReplyFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_delete_answer_failed);
    }

    @Override
    public void onSupportAnswerFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_support_answer_failed);
    }

    @Override
    public void onOpposeAnswerFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_oppose_answer_failed);
    }

    @Override
    protected ReplyViewHolderBase createReplyHolder(ViewGroup parent) {
        return new AnswerViewHolder(parent);
    }

    @Override
    protected ContentViewHolderBase createContentHolder(ViewGroup parent) {
        return new QuestionViewHolder(parent);
    }

    class QuestionViewHolder extends ContentViewHolder {
        @Bind(R.id.text_all_replies) TextView label;

        public QuestionViewHolder(ViewGroup parent) {
            super(parent);

            label.setText(R.string.label_all_answers);
        }
    }

    class AnswerViewHolder extends ReplyViewHolderBase {
        @Bind(R.id.btn_support) ImageButton support;
        @Bind(R.id.text_support_count) TextView supportCount;
        @Bind(R.id.btn_oppose) ImageButton oppose;
        @Bind(R.id.text_oppose_count) TextView opposeCount;

        public AnswerViewHolder(ViewGroup parent) {
            super(R.layout.item_answer, parent, R.menu.item_answer);

            support.setOnClickListener(v -> presenter.supportAnswer(data));
            oppose.setOnClickListener(v -> presenter.opposeAnswer(data));
        }

        @Override
        protected void onMenuCreated(Menu menu) {
            super.onMenuCreated(menu);

            menu.findItem(R.id.menu_support).setVisible(!data.hasSupported);
            menu.findItem(R.id.menu_oppose).setVisible(!data.hasOpposed);
        }

        protected boolean onMenuClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_support:
                    presenter.supportAnswer(data);
                    return true;
                case R.id.menu_oppose:
                    presenter.opposeAnswer(data);
                    return true;
            }
            return super.onMenuClick(menuItem);
        }

        @Override
        protected void onBind(Answer answer, int position) {
            super.onBind(answer, position);

            support.setEnabled(!answer.hasSupported);
            supportCount.setText(String.valueOf(answer.supportingsCount));
            oppose.setEnabled(!answer.hasOpposed);
            opposeCount.setText(String.valueOf(answer.opposingsCount));
        }
    }
}
