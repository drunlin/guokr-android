package com.github.drunlin.guokr.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.ArticleType;
import com.github.drunlin.guokr.presenter.MinisitePresenter;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.util.ViewUtils;
import com.github.drunlin.guokr.view.MinisiteView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.github.drunlin.guokr.util.JavaUtil.call;

/**
 * 科学人主界面，包含所有的文章列表。
 *
 * @author drunlin@outlook.com
 */
public class MinisiteFragment extends FragmentBase implements MinisiteView {
    @Bind(R.id.tabs) TabLayout tabLayout;
    @Bind(R.id.view_pager) ViewPager viewPager;
    @Bind(R.id.btn_expand_more) ImageButton expandableButton;

    private PagerAdapter pagerAdapter;

    private PopupWindow popupWindow;

    private List<ArticleType> articleTypes;

    private MinisitePresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = lifecycle.bind(MinisitePresenter.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_minisite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pagerAdapter = new PagerAdapter(getFragmentManager());

        expandableButton.setOnClickListener(v -> showLabelsPanel());
    }

    public void setSelectedTab(String key) {
        presenter.onDisplayArticleList(key);
    }

    @Override
    public void onNoSuchTypeError() {
        ToastUtils.showShortTimeToast(R.string.toast_find_article_type_failed);
    }

    /**
     * 弹出显示有所有文章类型的面板。
     */
    private void showLabelsPanel() {
        expandableButton.setImageResource(R.drawable.ic_expand_less);

        LabelsPanel labelsPanel = new LabelsPanel(getContext());
        labelsPanel.setTypes(articleTypes);
        labelsPanel.setSelectedIndex(tabLayout.getSelectedTabPosition());

        popupWindow = new PopupWindow(labelsPanel, MATCH_PARENT, WRAP_CONTENT);
        popupWindow.setFocusable(true);
        //noinspection deprecation
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(() -> {
            popupWindow = null;

            expandableButton.setImageResource(R.drawable.ic_expand_more);
        });
        popupWindow.showAsDropDown(tabLayout);
    }

    public void setTypes(List<ArticleType> types) {
        articleTypes = types;

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void displayAt(int position) {
        //noinspection ConstantConditions
        tabLayout.getTabAt(position).select();

        call(popupWindow, PopupWindow::dismiss);
    }

    /**
     * 显示文章列表的适配器。
     */
    private class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ArticleListFragment(articleTypes.get(position).key);
        }

        @Override
        public int getCount() {
            return articleTypes != null ? articleTypes.size() : 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return articleTypes.get(position).name;
        }
    }

    /**
     * 显示所有文章类型的面板。
     */
    class LabelsPanel extends FrameLayout implements OnCheckedChangeListener {
        /**用于显示类型按钮的容器。*/
        @Bind(R.id.flow_layout) FlowLayout flowLayout;

        /**当前被选中的按钮。*/
        private RadioButton selectedButton;

        public LabelsPanel(Context context) {
            super(context);

            inflate(context, R.layout.list_article_types, this);
            ButterKnife.bind(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton button, boolean isChecked) {
            if (isChecked) {
                setSelectedButton((RadioButton) button);

                displayAt((int) button.getTag());
            }
        }

        private void setSelectedButton(RadioButton button) {
            if (selectedButton != null) {
                selectedButton.setChecked(false);
            }
            selectedButton = button;
        }

        /**
         * 设置被选中类型的索引。
         * @param position
         */
        public void setSelectedIndex(int position) {
            setSelectedButton((RadioButton) flowLayout.getChildAt(position));
            selectedButton.setOnCheckedChangeListener(null);
            selectedButton.setChecked(true);
            selectedButton.setOnCheckedChangeListener(this);
        }

        /**
         * 设置要显示的类型列表。
         * @param types
         */
        public void setTypes(@NonNull List<ArticleType> types) {
            int index = -1;
            for (ArticleType type : types) {
                RadioButton button = (RadioButton) ViewUtils.inflate(
                        getContext(), R.layout.btn_article_type, flowLayout);
                button.setText(type.name);
                button.setTag(++index);
                button.setOnCheckedChangeListener(this);
                flowLayout.addView(button);
            }
        }
    }
}
