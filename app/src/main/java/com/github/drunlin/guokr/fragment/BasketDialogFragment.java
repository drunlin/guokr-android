package com.github.drunlin.guokr.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Basket;
import com.github.drunlin.guokr.presenter.BasketPresenter;
import com.github.drunlin.guokr.presenter.BasketPresenter.Factory;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.util.ViewUtils;
import com.github.drunlin.guokr.view.BasketView;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.github.drunlin.guokr.util.JavaUtil.call;

/**
 * 收藏到果篮操作的弹窗。
 *
 * @author drunlin@outlook.com
 */
public class BasketDialogFragment extends DialogFragmentBase implements BasketView {
    private static final String ARGUMENT_TITLE = "ARGUMENT_TITLE";
    private static final String ARGUMENT_URL = "ARGUMENT_URL";

    private SimpleAdapter<Basket> adapter;

    private BasketCreator basketCreator;
    private RecyclerView basketList;

    private AlertDialog creatorDialog;

    private List<Basket.Category> categories;

    private BasketPresenter presenter;

    public static BasketDialogFragment show(FragmentManager manager, String title, String url) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TITLE, title);
        arguments.putString(ARGUMENT_URL, url);

        BasketDialogFragment fragment = new BasketDialogFragment();
        fragment.setArguments(arguments);
        fragment.show(manager, "TAG_BASKET_DIALOG");
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        String title = arguments.getString(ARGUMENT_TITLE);
        String url = arguments.getString(ARGUMENT_URL);
        presenter = lifecycle.bind(Factory.class, f -> f.create(title, url));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        adapter = new SimpleAdapter<>((parent, viewType) -> new BasketViewHolder(parent));

        basketList = new RecyclerView(getContext());
        basketList.setAdapter(adapter);
        basketList.setLayoutManager(new LinearLayoutManager(getContext()));

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_add_to_basket)
                .setView(basketList)
                .setPositiveButton(R.string.btn_close, null)
                .setNeutralButton(R.string.btn_create_basket, null)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();

        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEUTRAL)
                .setOnClickListener(v -> presenter.onPreCreateBasket());
    }

    /**
     * 创建并弹出创建果篮所需的面板。
     */
    @Override
    public void onCreateBasket() {
        basketCreator = new BasketCreator(getContext());
        creatorDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_create_basket)
                .setView(basketCreator)
                .setPositiveButton(R.string.btn_ok, null)
                .setNegativeButton(R.string.btn_cancel, null)
                .setOnDismissListener(dialog -> creatorDialog = null)
                .show();

        ViewUtils.setEditTextActionButton(
                basketCreator.title,
                creatorDialog.getButton(BUTTON_POSITIVE),
                text -> createBasket());

        if (categories != null) {
            basketCreator.setCategories(categories);
            categories = null;
        }
    }

    private void createBasket() {
        presenter.createBasket(
                basketCreator.getCategoryId(),
                basketCreator.getTitle(),
                basketCreator.getDescription());
    }

    /**
     * 成功创建果篮后，关闭创建果篮的面板并滚动果篮列表到最后。
     */
    @Override
    public void onCreateBasketSucceed() {
        call(creatorDialog, AlertDialog::dismiss);

        basketList.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onCategoriesLoading() {
        ToastUtils.showShortTimeToast(R.string.toast_basket_categories_on_loading);
    }

    @Override
    public void setBaskets(List<Basket> baskets) {
        adapter.setData(baskets);
    }

    @Override
    public void onBasketsLoadFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_load_baskets_failed);
    }

    @Override
    public void onCategoriesLoadFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_load_basket_categories_failed);
    }

    @Override
    public void onCreateBasketFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_create_basket_failed);
    }

    @Override
    public void onFavorLinkFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_favor_link_failed);
    }

    @Override
    public void updateBasket(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void setCategories(List<Basket.Category> categories) {
        if (basketCreator == null) {
            this.categories = categories;
        } else {
            basketCreator.setCategories(categories);
        }
    }

    class BasketViewHolder extends SimpleAdapter.ItemViewHolder<Basket> {
        @Bind(R.id.text_name) TextView name;
        @Bind(R.id.btn_add) ImageButton add;

        public BasketViewHolder(ViewGroup parent) {
            super(getContext(), R.layout.item_basket, parent);

            add.setOnClickListener(v -> presenter.addToBasket(data.id));
        }

        @Override
        protected void onBind(Basket data, int position) {
            name.setText(data.title);
            add.setEnabled(!data.added);
        }
    }

    static class BasketCreator extends FrameLayout {
        @Bind(R.id.spinner_category) Spinner categorySelector;
        @Bind(R.id.edit_title) EditText title;
        @Bind(R.id.edit_description) EditText description;

        private final ArrayAdapter<Basket.Category> adapter;

        public BasketCreator(Context context) {
            super(context);

            inflate(context, R.layout.dialog_basket_creator, this);
            ButterKnife.bind(this);

            adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item);

            categorySelector.setAdapter(adapter);
        }

        public void setCategories(List<Basket.Category> categories) {
            adapter.addAll(categories);
        }

        public String getTitle() {
            return title.getText().toString();
        }

        public String getDescription() {
            return description.getText().toString();
        }

        public int getCategoryId() {
            Basket.Category item = (Basket.Category) categorySelector.getSelectedItem();
            return item != null ? item.id : 0;
        }
    }
}
