package com.github.drunlin.guokr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.Intents;
import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.fragment.AccountFragment.OnAvatarClickListener;
import com.github.drunlin.guokr.fragment.ForumFragment;
import com.github.drunlin.guokr.fragment.MinisiteFragment;
import com.github.drunlin.guokr.fragment.NoticeFragment;
import com.github.drunlin.guokr.fragment.QuestionListFragment;
import com.github.drunlin.guokr.presenter.MainPresenter;
import com.github.drunlin.guokr.view.MainView;
import com.github.drunlin.guokr.widget.SearchView;

import butterknife.Bind;

/**
 * 该App的主界面，包含了科学人，小组，问答这三个子界面。
 *
 * @author drunlin@outlook.com
 */
public class MainActivity extends ToolBarActivity
        implements MainView, OnNavigationItemSelectedListener, OnAvatarClickListener {

    private static final String BUNDLE_PAGE_POSITION = "BUNDLE_PAGE_POSITION";

    @Bind(R.id.drawer_layout) DrawerLayout drawer;
    @Bind(R.id.left_drawer) NavigationView leftNavigation;
    @Bind(R.id.text_navigation) TextView mainNavigation;
    @Bind(R.id.search_view) SearchView searchView;

    /**当前显示的子界面。*/
    private int pagePosition = -1;

    private FragmentManager fragmentManager;

    private MainPresenter presenter;

    public static Intent getViewArticlesIntent(String articleKey) {
        Intent intent = new Intent(App.getContext(), MainActivity.class);
        intent.setAction(Intents.ACTION_VIEW_ARTICLES);
        intent.putExtra(Intents.EXTRA_ARTICLE_KEY, articleKey);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = lifecycle.bind(MainPresenter.class);

        fragmentManager = getSupportFragmentManager();

        setContentView(R.layout.activity_main);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        leftNavigation.setNavigationItemSelectedListener(this);

        mainNavigation.setOnClickListener(v -> popupMainNavigationMenu());

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().add(new NoticeFragment(), null).commit();
        } else {
            setPagePosition(savedInstanceState.getInt(BUNDLE_PAGE_POSITION));
        }

        handleIntent(getIntent());
    }

    private void popupMainNavigationMenu() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.menu_anchor));
        popupMenu.inflate(R.menu.activity_main_navigation);
        popupMenu.setOnMenuItemClickListener(item -> {
            setPagePosition(item.getOrder());
            return true;
        });
        popupMenu.show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        onRestorePageState();
    }

    /**
     * 还原recreate时消失的状态。
     */
    private void onRestorePageState() {
        for (int i = 0; i < 3; ++i) {
            if (i != pagePosition) {
                hidePage(i);
            }
        }
    }

    private void hidePage(int position) {
        String tag = getPageTag(position);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager.beginTransaction().hide(fragment).commit();
        }
    }

    private String getPageTag(int position) {
        return "TAG_PAGE_" + position;
    }

    @Override
    public void setPagePosition(int position) {
        if (pagePosition == position) {
            return;
        }

        mainNavigation.setText(getResources().getTextArray(R.array.activity_main_tabs)[position]);

        hidePage(pagePosition);

        String tag = getPageTag(position);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            transaction.add(R.id.container, newPage(position), tag);
        } else {
            transaction.show(fragment);
        }

        pagePosition = position;

        transaction.commit();
    }

    private Fragment newPage(int position) {
        switch (position) {
            case 0:
                return new MinisiteFragment();
            case 1:
                return new ForumFragment();
            case 2:
                return new QuestionListFragment();
        }
        return null;
    }

    private void handleIntent(Intent intent) {
        if (intent.getAction().equals(Intents.ACTION_VIEW_ARTICLES)) {
            setPagePosition(0);

            String key = intent.getStringExtra(Intents.EXTRA_ARTICLE_KEY);
            MinisiteFragment fragment =
                    (MinisiteFragment) fragmentManager.findFragmentById(R.id.container);
            App.post(() -> fragment.setSelectedTab(key));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void viewHomePage(String url) {
        startActivity(Intents.openLinkInBrowser(url));
    }

    @Override
    public void setNightModeEnable(boolean enable) {
        App.getContext().setTheme(enable);

        MenuItem menuItem = leftNavigation.getMenu().findItem(R.id.nav_theme);
        if (enable) {
            menuItem.setTitle(R.string.nav_day_mode);
            menuItem.setIcon(R.drawable.ic_day_mode);
        } else {
            menuItem.setTitle(R.string.nav_night_mode);
            menuItem.setIcon(R.drawable.ic_night_mode);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_PAGE_POSITION, pagePosition);
    }

    @Override
    protected void onStop() {
        super.onStop();

        presenter.savePagePosition(pagePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home_page:
                presenter.onViewHomePage();
                return true;
            case R.id.menu_search:
                searchView.setStartPositionFromMenuItem(findViewById(R.id.menu_search));
                searchView.openSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.nav_message:
                startActivity(new Intent(this, MessageListActivity.class));
                return true;
            case R.id.nav_notice:
                startActivity(new Intent(this, NoticeListActivity.class));
                return true;
            case R.id.nav_theme:
                presenter.toggleDayNightMode();
                return true;
            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }
        return false;
    }

    @Override
    public void onAvatarClick() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
