package com.github.drunlin.guokr.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.drunlin.guokr.Intents;
import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Content;
import com.github.drunlin.guokr.bean.Reply;
import com.github.drunlin.guokr.fragment.BasketDialogFragment;
import com.github.drunlin.guokr.fragment.EditorFragment;
import com.github.drunlin.guokr.presenter.ContentPresenter;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.view.ContentView;
import com.github.drunlin.guokr.widget.CircleImageView;
import com.github.drunlin.guokr.widget.RichTextView;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter.ItemViewHolder;

import java.util.List;

import butterknife.Bind;

import static android.view.Gravity.RIGHT;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 文章，帖子，问答内容界面的基类。其中内容，回复，floatingMenu由子类提供。
 *
 * @author drunlin@outlook.com
 */
public abstract class ContentActivity
        <T extends Content, C extends Reply, P extends ContentPresenter>
        extends ListActivity implements ContentView<T, C> {

    @Bind(R.id.btn_favor) FloatingActionButton favorButton;
    @Bind(R.id.btn_reply) FloatingActionButton replyButton;
    @Bind(R.id.floating_menu) FloatingActionsMenu floatingMenu;
    @Bind(R.id.floating_menu_container) ViewGroup floatingMenuContainer;

    protected Adapter adapter;

    /**控制所有菜单的显示。*/
    protected boolean menuVisible;

    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = onCreatePresenter(getIntent().getAction(), getIntent());

        setContentView(R.layout.activity_content);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter = new Adapter();
        adapter.setOnLoadMoreListener(presenter::loadMoreReplies);

        swipeLoadLayout.setOnRefreshListener(presenter::refresh);
        swipeLoadLayout.setOnLoadMoreListener(presenter::loadMoreReplies);

        recyclerView.setAdapter(adapter);

        favorButton.setOnClickListener(v -> presenter.onFavor());
        replyButton.setOnClickListener(v -> presenter.onPreReply());
    }

    /**
     * 在这里初始化Presenter。
     * @param action
     * @param intent
     */
    protected abstract P onCreatePresenter(String action, Intent intent);

    /**
     * 获取FloatingMenu的布局资源。
     * @return
     */
    protected abstract @LayoutRes int getFloatingMenuRes();

    @Override
    protected void initialize() {
        View.inflate(this,
                getFloatingMenuRes(), (ViewGroup) findViewById(R.id.floating_menu_container));

        super.initialize();
    }

    @Override
    public void onBackPressed() {
        if (floatingMenu.isExpanded()) {
            floatingMenu.collapse();
            return;
        }

        EditorFragment editor = getEditor();
        if (editor != null) {
            presenter.onCancelReply(editor.getHtml());
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void setContent(@NonNull T data) {
        adapter.setContent(data);

        replyButton.setVisibility(data.isReplyable ? VISIBLE : GONE);

        floatingMenuContainer.setVisibility(VISIBLE);

        menuVisible = true;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_content, menu);
        return menuVisible;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                presenter.onShare();
                return true;
            case R.id.menu_open_link:
                presenter.onOpenLink();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setReplies(List<C> comments) {
        adapter.setReplies(comments);
    }

    @Override
    public void onRepliesAppended() {
        adapter.notifyDataAppended();
    }

    @Override
    public void updateReply(int position) {
        adapter.notifyItemChanged(position + 1);
    }

    @Override
    public void onLoadContentFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_load_content_failed);
    }

    @Override
    public void favor(String name, String url) {
        BasketDialogFragment.show(getSupportFragmentManager(), name, url);
    }

    /**
     * 准备回复，显示回复框。
     */
    @Override
    public void preReply() {
        if (getEditor() != null) {
            return;
        }

        EditorFragment editor = new EditorFragment();
        editor.setOnCompleteListener(content -> presenter.reply(content));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_reply, editor)
                .commit();
    }

    /**
     * 获取回复框。
     * @return
     */
    protected @Nullable EditorFragment getEditor() {
        return (EditorFragment) getSupportFragmentManager().findFragmentById(R.id.container_reply);
    }

    /**
     * 显示取消回复的对话框。
     */
    @Override
    public void cancelReply() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_cancel_reply))
                .setPositiveButton(R.string.btn_ok, (d, w) -> onReplyComplete())
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    /**
     * 回复完成，隐藏回复框。
     */
    @Override
    public void onReplyComplete() {
        Fragment fragment = getEditor();
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onReplyRemoved(int position) {
        adapter.notifyItemRemoved(position + 1);
    }

    @Override
    public void openLink(String url) {
        startActivity(Intents.openLinkInBrowser(url));
    }

    @Override
    public void share(String content) {
        Intent intent=new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(Intent.createChooser(intent, getString(R.string.title_share)));
    }

    /**
     * 获取回复的视图容器。
     * @param parent
     * @return
     */
    protected abstract ReplyViewHolderBase createReplyHolder(ViewGroup parent);

    /**
     * 创建内容视图容器。
     * @param parent
     * @return
     */
    protected abstract ContentViewHolderBase createContentHolder(ViewGroup parent);

    /**
     * 内容视图容器基类。
     */
    protected abstract class ContentViewHolderBase extends ItemViewHolder<T> {
        @Bind(R.id.text_title) TextView title;
        @Bind(R.id.text_date) TextView date;
        @Bind(R.id.text_content) RichTextView content;

        public ContentViewHolderBase(@LayoutRes int layoutRes, ViewGroup parent) {
            super(ContentActivity.this, layoutRes, parent);
        }

        @CallSuper
        @Override
        protected void onBind(T data, int position) {
            title.setText(data.title);
            date.setText(data.dateCreated);
            content.setHtml(data.getContent());
        }
    }

    /**
     * 帖子，问答内容通用的视图容器。
     */
    protected class ContentViewHolder extends ContentViewHolderBase {
        @Bind(R.id.img_avatar) CircleImageView avatar;
        @Bind(R.id.text_author) TextView author;

        public ContentViewHolder(ViewGroup parent) {
            super(R.layout.item_topic_content, parent);
        }

        @Override
        protected void onBind(T data, int position) {
            super.onBind(data, position);

            avatar.setImageData(data.author.avatar.data);
            author.setText(data.author.nickname);
        }
    }

    /**
     * 回复视图容器基类。
     */
    protected abstract class ReplyViewHolderBase extends ItemViewHolder<C> {
        @Bind(R.id.img_avatar) CircleImageView avatar;
        @Bind(R.id.text_author) TextView author;
        @Bind(R.id.text_count) TextView count;
        @Bind(R.id.text_date) TextView date;
        @Bind(R.id.text_content) RichTextView content;
        @Bind(R.id.menu_anchor) View menuAnchor;

        private final int menuRes;

        public ReplyViewHolderBase(
                @LayoutRes int layoutRes, ViewGroup parent, @MenuRes int menuRes) {

            super(ContentActivity.this, layoutRes, parent);

            this.menuRes = menuRes;

            itemView.setOnClickListener(v -> popupMenu());
        }

        private void popupMenu() {
            @SuppressLint("RtlHardcoded")
            PopupMenu menu = new PopupMenu(ContentActivity.this, menuAnchor, RIGHT);
            menu.inflate(menuRes);
            onMenuCreated(menu.getMenu());
            menu.setOnMenuItemClickListener(this::onMenuClick);
            menu.show();
        }

        @CallSuper
        protected void onMenuCreated(Menu menu) {
            menu.findItem(R.id.menu_delete).setVisible(data.isAuthor);
        }

        @CallSuper
        protected boolean onMenuClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_copy:
                    presenter.copyReplyContent(data);
                    return true;
                case R.id.menu_delete:
                    presenter.deleteReply(data);
                    return true;
            }
            return false;
        }

        @CallSuper
        @Override
        protected void onBind(C comment, int position) {
            avatar.setImageData(comment.author.avatar.data);
            author.setText(comment.author.nickname);
            count.setText(getString(R.string.label_post_count, position + 1));
            date.setText(comment.dateCreated);
            content.setHtml(comment.content);
        }
    }

    /**
     * 内容和评论显示在同一个列表内。
     */
    private class Adapter extends SimpleAdapter<C> {
        private final int TYPE_CONTENT = 0;
        private final int TYPE_REPLY = 1;

        private T contentData;

        public Adapter() {
            super(null);
        }

        /**
         * 设置内容的数据。
         * @param data
         */
        public void setContent(T data) {
            contentData = data;

            notifyItemChanged(0);
        }

        /**
         * 设置回复列表。必须在{@link #setContent(Content)}之后调用。
         * @param replies
         */
        public void setReplies(List<C> replies) {
            data = replies;

            currentDataSize = getItemCount();

            notifyItemRangeChanged(1, currentDataSize);
        }

        @Deprecated
        @Override
        public void setData(List<C> data) {
            setReplies(data);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_CONTENT) {
                return createContentHolder(parent);
            } else {
                return createReplyHolder(parent);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_CONTENT) {
                //noinspection unchecked
                ((ItemViewHolder<T>) holder).setData(contentData, position);
            } else {
                super.onBindViewHolder(holder, position - 1);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? TYPE_CONTENT : TYPE_REPLY;
        }

        @Override
        public int getItemCount() {
            return contentData != null ? super.getItemCount() + 1 : 0;
        }
    }

    /**
     * 内容显示区域的布局行为。
     */
    @SuppressWarnings("unused")
    public static class ContentBehavior extends AppBarLayout.ScrollingViewBehavior {
        public ContentBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
            return dependency.getId() == R.id.container_reply
                    || super.layoutDependsOn(parent, child, dependency);
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
            super.onLayoutChild(parent, child, layoutDirection);

            View replayBox = parent.findViewById(R.id.container_reply);
            if (replayBox != null) {
                int height = child.getMeasuredHeight() - replayBox.getMeasuredHeight();
                int width = child.getMeasuredWidth();
                child.findViewById(R.id.recycler_view).layout(0, 0, width, height);
                return true;
            }
            return false;
        }
    }

    /**
     * 回复框的布局行为。
     */
    @SuppressWarnings("unused")
    public static class ReplyBoxBehavior extends CoordinatorLayout.Behavior<ViewGroup> {
        public ReplyBoxBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onMeasureChild(CoordinatorLayout parent, ViewGroup child,
                                      int widthMeasureSpec, int widthUsed,
                                      int heightMeasureSpec, int heightUsed) {
            Activity activity = (Activity) parent.getContext();
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int height = MeasureSpec.getSize(heightMeasureSpec) + heightUsed;
            height = Math.min(metrics.heightPixels / 5 * 2, height);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
            child.measure(widthMeasureSpec, heightMeasureSpec);
            return true;
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, ViewGroup child, int layoutDirection) {
            child.layout(0, parent.getMeasuredHeight() - child.getMeasuredHeight(),
                    parent.getMeasuredWidth(), parent.getMeasuredHeight());
            return true;
        }
    }

    /**
     * {@link FloatingActionsMenu}的交互行为。注意该类是用在它的容器上面的。
     */
    @SuppressWarnings("unused")
    public static class FloatingMenuBehavior extends CoordinatorLayout.Behavior<ViewGroup> {
        private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

        private boolean visible = true;
        private boolean isAnimatingOut = false;

        public FloatingMenuBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, ViewGroup child, View dependency) {
            return dependency instanceof Snackbar.SnackbarLayout
                    || dependency.getId() == R.id.container_reply;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, ViewGroup child, View dependency) {
            if (dependency instanceof Snackbar.SnackbarLayout) {
                float translationY = Math.min(
                        0, ViewCompat.getTranslationY(dependency) - dependency.getMeasuredHeight());
                ViewCompat.setTranslationY(child, translationY);
                return true;
            } else {
                boolean show = dependency.getMeasuredHeight() == 0;
                if (show != visible) {
                    if (show) {
                        showMenu(child);
                    } else {
                        visible = false;
                        getFloatingActionsMenu(child).setVisibility(GONE);
                    }
                }
                return false;
            }
        }

        private FloatingActionsMenu getFloatingActionsMenu(ViewGroup container) {
            return (FloatingActionsMenu) container.getChildAt(0);
        }

        private void showMenu(ViewGroup child) {
            visible = true;
            getFloatingActionsMenu(child).setVisibility(VISIBLE);
        }

        @Override
        public void onDependentViewRemoved(CoordinatorLayout parent,
                                           ViewGroup child, View dependency) {
            if (dependency instanceof Snackbar.SnackbarLayout) {
                ViewCompat.animate(child)
                        .translationY(0.0F)
                        .scaleX(1.0F)
                        .scaleY(1.0F)
                        .alpha(1.0F)
                        .setInterpolator(INTERPOLATOR)
                        .start();
            } else if (!visible){
                showMenu(child);
            }
        }

        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, ViewGroup child, MotionEvent ev) {
            FloatingActionsMenu menu = getFloatingActionsMenu(child);
            if (!(visible && menu.isExpanded())) {
                return false;
            }

            final int x = (int) ev.getX();
            final int y = (int) ev.getY();

            Rect rect = new Rect();
            child.getHitRect(rect);

            if (rect.contains(x, y)) {
                if (ev.getAction() == MotionEvent.ACTION_UP) {
                    //点击非"加"按钮就折叠菜单
                    menu.getChildAt(menu.getChildCount() - 1).getHitRect(rect);
                    if (!rect.contains(x - (int) child.getX(), y - (int) child.getY())) {
                        menu.collapse();
                    }
                }
                return false;
            } else {
                menu.collapse();
                return true;
            }
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                           ViewGroup child, View directTargetChild,
                                           View target, int nestedScrollAxes) {
            return visible && nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout,
                                   ViewGroup child, View target,
                                   int dxConsumed, int dyConsumed,
                                   int dxUnconsumed, int dyUnconsumed) {
            FloatingActionsMenu menu = getFloatingActionsMenu(child);
            if (dyConsumed > 0 && !isAnimatingOut && menu.getVisibility() == VISIBLE){
                animateOut(menu);
            } else if (dyConsumed < 0 && menu.getVisibility() != VISIBLE){
                animateIn(menu);
            }
        }

        private void animateOut(View view){
            ViewCompat.animate(view).translationY(500)
                    .setInterpolator(INTERPOLATOR).withLayer()
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                            isAnimatingOut = true;
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            isAnimatingOut = false;
                            view.setVisibility(GONE);
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                            isAnimatingOut = false;

                        }
                    }).start();
        }

        private void animateIn(View view){
            view.setVisibility(VISIBLE);
            ViewCompat.animate(view).translationY(0)
                    .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
                    .start();
        }
    }
}
