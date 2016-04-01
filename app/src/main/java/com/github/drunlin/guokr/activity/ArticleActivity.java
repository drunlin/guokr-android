package com.github.drunlin.guokr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.Intents;
import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.ArticleContent;
import com.github.drunlin.guokr.fragment.RecommendDialogFragment;
import com.github.drunlin.guokr.presenter.ArticlePresenter;
import com.github.drunlin.guokr.presenter.ArticlePresenter.Factory;
import com.github.drunlin.guokr.view.ArticleView;
import com.github.drunlin.guokr.widget.ArticleAuthorsView;
import com.github.drunlin.guokr.widget.ArticleLabelsView;

import butterknife.Bind;

/**
 * 显示文章内容和评论，处理与单个文章相关的操作。
 *
 * @author drunlin@outlook.com
 */
public class ArticleActivity
        extends TopicActivity<ArticleContent, ArticlePresenter> implements ArticleView {

    @Bind(R.id.btn_recommend) FloatingActionButton recommendButton;

    private boolean scrollToReplies;

    /**
     * 浏览文章内容。
     * @param articleId
     * @return
     */
    public static Intent getViewArticleIntent(int articleId) {
        Intent intent = new Intent(App.getContext(), ArticleActivity.class);
        intent.setAction(Intents.ACTION_VIEW_ARTICLE);
        intent.putExtra(Intents.EXTRA_ARTICLE_ID, articleId);
        return intent;
    }

    /**
     * 浏览文章的评论。在数据加载完后会自动滚动到评论区。
     * @param articleId
     * @return
     */
    public static Intent getViewRepliesIntent(int articleId) {
        Intent intent = new Intent(App.getContext(), ArticleActivity.class);
        intent.setAction(Intents.ACTION_VIEW_REPLIES);
        intent.putExtra(Intents.EXTRA_ARTICLE_ID, articleId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recommendButton.setOnClickListener(v -> presenter.onRecommend());
    }

    @Override
    protected ArticlePresenter onCreatePresenter(String action, Intent intent) {
        int articleId;
        switch (action) {
            case Intent.ACTION_VIEW:
                articleId = Intents.getIdFromUri(intent, "article");
                break;
            case Intents.ACTION_VIEW_REPLIES:
                scrollToReplies = true;
            default:
                articleId = getIntent().getIntExtra(Intents.EXTRA_ARTICLE_ID, 0);
        }
        return lifecycle.bind(Factory.class, f -> f.create(articleId));
    }

    @Override
    protected int getFloatingMenuRes() {
        return R.layout.floating_menu_simple;
    }

    /**
     * 实现滚动到评论区的功能，如果只是滚动一次，那么内容图片加载完成后的高度变化会影响体验。
     * @return
     */
    @Override
    protected RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(this) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                if (scrollToReplies) {
                    recyclerView.scrollToPosition(1);
                }

                super.onLayoutChildren(recycler, state);
            }
        };
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //触摸屏幕后立即取消自动滚动到评论区的操作
        if (scrollToReplies) {
            scrollToReplies = false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void recommend(String title, String summary, String url) {
        RecommendDialogFragment.show(getSupportFragmentManager(), title, summary, url);
    }

    @Override
    protected ContentViewHolderBase createContentHolder(ViewGroup parent) {
        return new ContentViewHolder(parent);
    }

    class ContentViewHolder extends ContentViewHolderBase {
        @Bind(R.id.text_labels) ArticleLabelsView labels;
        @Bind(R.id.text_author) ArticleAuthorsView authors;

        public ContentViewHolder(ViewGroup parent) {
            super(R.layout.item_article_content, parent);
        }

        @Override
        protected void onBind(ArticleContent data, int position) {
            super.onBind(data, position);

            labels.setLabels(data.labels);
            authors.setAuthors(data.authors);
        }
    }
}
