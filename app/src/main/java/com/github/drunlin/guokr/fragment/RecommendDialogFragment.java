package com.github.drunlin.guokr.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.presenter.RecommendPresenter;
import com.github.drunlin.guokr.presenter.RecommendPresenter.Factory;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.view.RecommendView;

/**
 * 用于推荐给关注的人的对话框。
 *
 * @author drunlin@outlook.com
 */
public class RecommendDialogFragment extends DialogFragmentBase implements RecommendView {
    private static final String ARGUMENT_URL = "ARGUMENT_URL";
    private static final String ARGUMENT_TITLE = "ARGUMENT_TITLE";
    private static final String ARGUMENT_SUMMARY = "ARGUMENT_SUMMARY";

    private EditText editText;

    private RecommendPresenter presenter;

    public static RecommendDialogFragment show(FragmentManager manager,
                                               String title, String summary, String url) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TITLE, title);
        arguments.putString(ARGUMENT_SUMMARY, summary);
        arguments.putString(ARGUMENT_URL, url);

        RecommendDialogFragment fragment = new RecommendDialogFragment();
        fragment.setArguments(arguments);
        fragment.show(manager, "TAG_RECOMMEND_DIALOG");
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        String url = arguments.getString(ARGUMENT_URL);
        String title = arguments.getString(ARGUMENT_TITLE);
        String summary = arguments.getString(ARGUMENT_SUMMARY);
        presenter = lifecycle.bind(Factory.class, f -> f.create(url, title, summary));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_recommend)
                .setView(R.layout.dialog_recommend)
                .setPositiveButton(R.string.btn_recommend, null)
                .setNegativeButton(R.string.btn_cancel, null)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog dialog = (AlertDialog) getDialog();

        editText = (EditText) dialog.findViewById(R.id.edit_comment);

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> presenter.recommend(editText.getText().toString()));
    }

    @Override
    public void onRecommendSucceed() {
        dismiss();
    }

    @Override
    public void onRecommendFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_recommend_failed);
    }
}
