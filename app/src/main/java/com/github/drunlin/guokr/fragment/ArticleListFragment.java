package com.github.drunlin.guokr.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.activity.ArticleActivity;
import com.github.drunlin.guokr.activity.MainActivity;
import com.github.drunlin.guokr.bean.ArticleEntry;
import com.github.drunlin.guokr.bean.Image;
import com.github.drunlin.guokr.bean.ImageData;
import com.github.drunlin.guokr.presenter.ArticleListPresenter;
import com.github.drunlin.guokr.presenter.ArticleListPresenter.Factory;
import com.github.drunlin.guokr.view.ArticleListView;
import com.github.drunlin.guokr.widget.ArticleAuthorsView;
import com.github.drunlin.guokr.widget.ArticleLabelsView;
import com.github.drunlin.guokr.widget.BitmapImageView;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter.ItemViewHolder;

import butterknife.Bind;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * 单个分类下的文章列表界面。
 *
 * @author drunlin@outlook.com
 */
public class ArticleListFragment extends
        TopicListFragment<ArticleEntry, ArticleListPresenter> implements ArticleListView {

    private static final String ARGUMENT_ARTICLE_KEY = "ARGUMENT_ARTICLE_KEY";

    @Deprecated
    public ArticleListFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public ArticleListFragment(String key) {
        Bundle argument = new Bundle();
        argument.putString(ARGUMENT_ARTICLE_KEY, key);
        setArguments(argument);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String key = getArguments().getString(ARGUMENT_ARTICLE_KEY);
        presenter = lifecycle.bind(Factory.class, f -> f.create(key));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter.setSpanCount(getColumn());
    }

    /**
     * 获取列表的列数，根据屏幕宽度变化而变化。
     * @return
     */
    private int getColumn() {
        return getResources().getInteger(R.integer.article_list_column);
    }

    @Override
    protected LayoutManager onCreateLayoutManager() {
        return new StaggeredGridLayoutManager(getColumn(), OrientationHelper.VERTICAL);
    }

    @Override
    protected ItemViewHolder<ArticleEntry> onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(parent);
    }

    @Override
    public void viewArticle(int articleId) {
        startActivity(ArticleActivity.getViewArticleIntent(articleId));
    }

    @Override
    public void viewArticleReplies(int articleId) {
        startActivity(ArticleActivity.getViewRepliesIntent(articleId));
    }

    @Override
    public void viewArticles(String key) {
        startActivity(MainActivity.getViewArticlesIntent(key));
    }

    class ViewHolder extends ItemViewHolder<ArticleEntry> {
        @Bind(R.id.labels) ArticleLabelsView labels;
        @Bind(R.id.text_title) TextView title;
        @Bind(R.id.text_authors) ArticleAuthorsView authors;
        @Bind(R.id.text_date) TextView date;
        @Bind(R.id.btn_reply) ImageButton reply;
        @Bind(R.id.text_replies_count) TextView repliesCount;
        @Bind(R.id.image) BitmapImageView image;
        @Bind(R.id.text_summary) TextView summary;

        private ImageData.Loader loader;

        public ViewHolder(ViewGroup parent) {
            super(getContext(), R.layout.item_article, parent);

            itemView.setOnClickListener(v -> presenter.onViewArticle(data));
            reply.setOnClickListener(v -> presenter.onViewArticleReplies(data));
            labels.setOnLabelClickListener(type -> presenter.onViewArticles(type));
        }

        @Override
        protected void onBind(ArticleEntry entry, int position) {
            labels.setLabels(entry.labels);
            title.setText(entry.title);
            authors.setAuthors(entry.authors);
            date.setText(entry.dateCreated);
            repliesCount.setText(String.valueOf(entry.repliesCount));
            summary.setText(entry.summary);

            initImage(entry);
        }

        /**
         * 初始化图像的大小，防止大小变化而出现“抖动”。
         * @param entry
         */
        private void initImage(ArticleEntry entry) {
            final Image img = entry.image;
            final int width = image.getMeasuredWidth();
            if (width == 0) {
                itemView.setVisibility(INVISIBLE);
                image.post(() -> {
                    setImageHeight(img, image.getMeasuredWidth());
                    itemView.setVisibility(VISIBLE);
                });
            } else {
                setImageHeight(img, width);
            }
            loader = img.data.get(image::setImageData);
        }

        private void setImageHeight(Image img, int width) {
            image.setMinimumHeight(width * img.height / img.width);
        }

        @Override
        protected void onRecycle() {
            loader.cancel();
            loader = null;

            image.setImageData(null);
        }
    }
}
