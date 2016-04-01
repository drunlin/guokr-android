package com.github.drunlin.guokr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.activity.QuestionActivity;
import com.github.drunlin.guokr.bean.QuestionEntry;
import com.github.drunlin.guokr.presenter.QuestionListPresenter;
import com.github.drunlin.guokr.view.QuestionListView;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter.ItemViewHolder;

import butterknife.Bind;

/**
 * 热门问答下的问题列表。
 *
 * @author drunlin@outlook.com
 */
public class QuestionListFragment extends
        TopicListFragment<QuestionEntry, QuestionListPresenter> implements QuestionListView {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = lifecycle.bind(QuestionListPresenter.class);
    }

    @Override
    protected ItemViewHolder<QuestionEntry> onCreateViewHolder(ViewGroup parent) {
        return new ItemHolder(parent);
    }

    @Override
    public void viewQuestion(int questionId) {
        startActivity(QuestionActivity.getIntent(questionId));
    }

    class ItemHolder extends ItemViewHolder<QuestionEntry> {
        @Bind(R.id.text_title) TextView title;
        @Bind(R.id.text_summary) TextView summary;

        public ItemHolder(ViewGroup parent) {
            super(getContext(), R.layout.item_question, parent);

            itemView.setOnClickListener(v -> presenter.onViewQuestion(data));
        }

        @Override
        protected void onBind(QuestionEntry data, int position) {
            title.setText(data.question);
            summary.setText(data.summary);
        }
    }
}
