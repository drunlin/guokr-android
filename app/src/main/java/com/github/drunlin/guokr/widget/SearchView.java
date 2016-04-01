package com.github.drunlin.guokr.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

import com.github.drunlin.guokr.R;

import org.cryse.widget.persistentsearch.LogoView;
import org.cryse.widget.persistentsearch.PersistentSearchView;

import butterknife.Bind;
import butterknife.ButterKnife;

import static org.cryse.widget.persistentsearch.R.id;

/**
 *  Material风格的搜索框。
 *
 * @author drunlin@outlook.com
 */
public class SearchView extends PersistentSearchView {
    private static final String BUNDLE_SUPER_STATE = "BUNDLE_SUPER_STATE";
    private static final String BUNDLE_LOGO_TEXT = "BUNDLE_LOGO_TEXT";

    @Bind(id.logoview) LogoView logoView;
    @Bind(id.button_home) View homeButton;
    @Bind(id.cardview_search) CardView background;

    private HomeButtonListener homeButtonListener;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        ButterKnife.bind(this);

        //改变背景色以适应主题
        background.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.cardBackgroundColor));

        homeButton.setOnClickListener(v -> {
            if (isEditing()) {
                cancelEditing();
            } else if (homeButtonListener != null) {
                homeButtonListener.onHomeButtonClick();
            }
        });
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_SUPER_STATE, super.onSaveInstanceState());
        bundle.putString(BUNDLE_LOGO_TEXT, logoView.getText().toString());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            logoView.setText(bundle.getString(BUNDLE_LOGO_TEXT));
            state = bundle.getParcelable(BUNDLE_SUPER_STATE);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public void setSearchString(String text, boolean avoidTriggerTextWatcher) {
        super.setSearchString(text, avoidTriggerTextWatcher);

        logoView.setText(text);
    }

    @Override
    public void setHomeButtonListener(HomeButtonListener listener) {
        homeButtonListener = listener;
    }
}
