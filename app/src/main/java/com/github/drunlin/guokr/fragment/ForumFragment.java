package com.github.drunlin.guokr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.presenter.ForumPresenter;
import com.github.drunlin.guokr.view.ForumView;

import butterknife.Bind;

import static com.github.drunlin.guokr.model.ForumModel.HOT_POST;
import static com.github.drunlin.guokr.model.ForumModel.MY_GROUP_POST;

/**
 * 小组的主界面包含我的小组，小组热帖，加入的小组。
 *
 * @author drunlin@outlook.com
 */
public class ForumFragment extends FragmentBase implements ForumView {
    private static final String BUNDLE_LOGIN = "BUNDLE_LOGIN";

    @Bind(R.id.tabs) TabLayout tabLayout;
    @Bind(R.id.pager) ViewPager viewPager;

    private Adapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lifecycle.bind(ForumPresenter.class);

        adapter = new Adapter(getFragmentManager());
        if (savedInstanceState != null) {
            adapter.setLoginStatus(savedInstanceState.getBoolean(BUNDLE_LOGIN));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(BUNDLE_LOGIN, adapter.loggedIn);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 没有登录时只显示小组热帖。
     * @param loggedIn
     */
    @Override
    public void setLoginStatus(boolean loggedIn) {
        tabLayout.setVisibility(loggedIn ? View.VISIBLE : View.GONE);

        adapter.setLoginStatus(loggedIn);
    }

    class Adapter extends FragmentStatePagerAdapter {
        /**登录时显示的所有tab。*/
        private final String[] tabs = getResources().getStringArray(R.array.group_tabs);
        /**没有登录时只显示小组热帖。*/
        private final String[] singleTab = new String[] {tabs[1]};

        private String[] currentTabs;

        private boolean loggedIn;

        public Adapter(FragmentManager fm) {
            super(fm);

            currentTabs = singleTab;
        }

        public void setLoginStatus(boolean login) {
            if (loggedIn != login) {
                loggedIn = login;

                currentTabs = login ? tabs : singleTab;

                notifyDataSetChanged();
            }
        }

        @Override
        public Fragment getItem(int position) {
            final CharSequence title = getPageTitle(position);
            if (tabs[0].equals(title)) {
                return new PostListFragment(MY_GROUP_POST);
            } else if(tabs[2].equals(title)) {
                return new GroupListFragment();
            } else {
                return new PostListFragment(HOT_POST);
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return currentTabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return currentTabs[position];
        }
    }
}
