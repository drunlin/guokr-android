package com.github.drunlin.guokr.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceResponse;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Thumbnail;
import com.github.drunlin.guokr.presenter.EditorPresenter;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.util.ViewUtils;
import com.github.drunlin.guokr.view.EditorView;
import com.github.drunlin.guokr.widget.RichEditorView;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

import static android.app.Activity.RESULT_OK;
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.support.v4.content.ContextCompat.getColor;
import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.github.drunlin.guokr.bean.Thumbnail.FAILED;
import static com.github.drunlin.guokr.bean.Thumbnail.LOADING;
import static com.github.drunlin.guokr.util.JavaUtil.call;

/**
 * 用于回复的文本编辑器。
 *
 * @author drunlin@outlook.com
 */
public class EditorFragment extends FragmentBase implements EditorView {
    private static final String BUNDLE_PICTURE_URI = "BUNDLE_PICTURE_URI";
    private static final String BUNDLE_PHOTO_PATH = "BUNDLE_PHOTO_PATH";

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_IMAGE_SELECT = 2;
    public static final int REQUEST_READ_EXTERNAL_STORAGE = 3;

    private OnCompleteListener onCompleteListener;

    @Bind(R.id.ck_format_bold) ImageButton formatBold;
    @Bind(R.id.ck_format_italic) ImageButton formatItalic;
    @Bind(R.id.btn_insert_photo) ImageButton insertPhoto;
    @Bind(R.id.btn_insert_link) ImageButton insertLink;
    @Bind(R.id.ck_format_quote) ImageButton formatQuote;
    @Bind(R.id.ck_format_list_bulleted) ImageButton formatListBulleted;
    @Bind(R.id.ck_format_list_numbered) ImageButton formatListNumbered;
    @Bind(R.id.btn_format_clear) ImageButton formatClear;
    @Bind(R.id.btn_undo) ImageButton undo;
    @Bind(R.id.btn_redo) ImageButton redo;
    @Bind(R.id.btn_clear) ImageButton clear;
    @Bind(R.id.edit_editor) RichEditorView editor;
    @Bind(R.id.list_images) RecyclerView images;
    @Bind(R.id.btn_send) ImageButton send;

    private SimpleAdapter<Thumbnail> adapter;

    private Map<String, InputStream> imageMap;

    /**请求权限时暂存的图片信息。*/
    private Uri currentPictureUri;
    /**用相机拍照时的临时路径。*/
    private String currentPhotoPath;

    private AlertDialog imageChooser;
    private AlertDialog pictureLinkDialog;
    private AlertDialog linkDialog;

    /**每次onCreateView都返回它。*/
    private View view;

    private EditorPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = lifecycle.bind(EditorPresenter.class);

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_editor, container, false);
        } else {
            call(view.getParent(), parent -> ((ViewGroup) parent).removeView(view));
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageMap = new ConcurrentHashMap<>();

        adapter = new SimpleAdapter<>((parent, t) -> new ViewHolder(parent));

        images.setAdapter(adapter);
        images.setLayoutManager(new LinearLayoutManager(getContext(), HORIZONTAL, false));

        formatBold.setOnClickListener(v -> editor.setBold());
        formatItalic.setOnClickListener(v -> editor.setItalic());

        insertLink.setOnClickListener(v -> popupInsertLinkDialog());
        insertPhoto.setOnClickListener(v -> popupPictureChooser());

        formatQuote.setOnClickListener(v -> editor.setBlockquote());
        formatListBulleted.setOnClickListener(v-> editor.setBullets());
        formatListNumbered.setOnClickListener(v -> editor.setNumbers());
        formatClear.setOnClickListener(v -> editor.removeFormat());

        undo.setOnClickListener(v -> editor.undo());
        redo.setOnClickListener(v -> editor.redo());
        clear.setOnClickListener(v -> editor.clear());

        send.setEnabled(false);
        send.setOnClickListener(v -> call(onCompleteListener, l -> l.onComplete(editor.getHtml())));

        editor.setEditorFontSize(18);
        editor.setEditorFontColor(getContext().getTheme()
                .obtainStyledAttributes(new int[] {android.R.attr.textColorPrimary})
                .getColor(0, Color.WHITE));
        editor.setEditorBackgroundColor(getColor(getContext(), R.color.cardBackgroundColor));
        editor.setOnInterceptRequestListener(
                url -> new WebResourceResponse("image/png", null, imageMap.remove(url)));
        editor.setOnTextChangeListener(text -> send.setEnabled(!editor.isEmpty()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(BUNDLE_PHOTO_PATH, currentPhotoPath);
        outState.putParcelable(BUNDLE_PICTURE_URI, currentPictureUri);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            currentPhotoPath = savedInstanceState.getString(BUNDLE_PHOTO_PATH);
            currentPictureUri = savedInstanceState.getParcelable(BUNDLE_PICTURE_URI);
        }
    }

    @SuppressWarnings("all")
    private void popupInsertLinkDialog() {
        linkDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_insert_link)
                .setView(R.layout.dialog_link)
                .setPositiveButton(R.string.btn_insert, null)
                .setNegativeButton(R.string.btn_cancel, null)
                .setOnDismissListener(dialog -> linkDialog = null)
                .show();

        EditText name = (EditText) linkDialog.findViewById(R.id.edit_name);
        EditText url = (EditText) linkDialog.findViewById(R.id.edit_url);

        Button button = linkDialog.getButton(BUTTON_POSITIVE);
        button.setEnabled(false);
        button.setOnClickListener(v -> presenter.onInsertLink(getText(name), getText(url)));

        TextWatcher watcher = new ViewUtils.SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                button.setEnabled(
                        !ViewUtils.isEmptyEditText(name) && !ViewUtils.isEmptyEditText(url));
            }
        };
        name.addTextChangedListener(watcher);
        url.addTextChangedListener(watcher);
    }

    private String getText(EditText editText) {
        return editText.getText().toString();
    }

    private void popupPictureChooser() {
        imageChooser = new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_pick_photo)
                .setItems(R.array.insert_photo_items, null)
                .setNegativeButton(R.string.btn_cancel, null)
                .setOnDismissListener(dialog -> imageChooser = null)
                .show();

        imageChooser.getListView().setOnItemClickListener(
                (parent, view, position, id) -> selectPhoto(position));
    }

    private void selectPhoto(int position) {
        switch (position) {
            case 0:
                popupPictureLinkDialog();
                break;
            case 1:
                onPickPhoto();
                break;
            case 2:
                onTakePhoto();
                break;
        }
    }
    
    private void popupPictureLinkDialog() {
        pictureLinkDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_insert_picture)
                .setView(R.layout.dialog_image_link)
                .setNeutralButton(R.string.btn_dialog_preview, null)
                .setPositiveButton(R.string.btn_insert, null)
                .setNegativeButton(R.string.btn_cancel, null)
                .setOnDismissListener(dialog -> pictureLinkDialog = null)
                .show();

        EditText url = (EditText) pictureLinkDialog.findViewById(R.id.edit_url);
        ViewUtils.setEditTextActionButton(url,
                pictureLinkDialog.getButton(BUTTON_NEUTRAL), presenter::insertPicture);
        ViewUtils.setEditTextActionButton(url,
                pictureLinkDialog.getButton(BUTTON_POSITIVE), presenter::onInsertPicture);
    }

    /**
     * 直接插入图片链接。
     * @param url
     */
    @Override
    public void insertPicture(String url) {
        String alt = url.length() > 20
                ? url.substring(0, 10) + "..." + url.substring(url.length() - 10) : url;
        editor.insertImage(url, alt);

        call(pictureLinkDialog, AlertDialog::dismiss);
        call(imageChooser, AlertDialog::dismiss);
    }

    @Override
    public void onPictureUrlValid() {
        call(pictureLinkDialog, AlertDialog::dismiss);
        call(imageChooser, AlertDialog::dismiss);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                onPickPhotoResult(resultCode);
                break;
            case REQUEST_IMAGE_SELECT:
                onPickPhotoResult(resultCode, data);
                break;
        }
    }

    private File createImageFile() throws IOException {
        File image = File.createTempFile(
                "" + System.currentTimeMillis(), ".jpg", getContext().getExternalCacheDir());
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void onTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File file = createImageFile();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } catch (IOException | ActivityNotFoundException e) {
            e.printStackTrace();
            ToastUtils.showShortTimeToast(R.string.toast_take_photo_failed);
        }
    }

    private void onPickPhotoResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            try {
                presenter.insertPicture(new FileInputStream(currentPhotoPath));

                call(imageChooser, AlertDialog::dismiss);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void onPickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        try {
            startActivityForResult(intent, REQUEST_IMAGE_SELECT);
        } catch (ActivityNotFoundException e) {
            ToastUtils.showShortTimeToast(R.string.toast_launch_gallery_failed);
        }
    }

    private void onPickPhotoResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (checkPermission()) {
                loadPicture(uri);
            } else {
                currentPictureUri = uri;
            }
        }
    }

    private void loadPicture(Uri uri) {
        try {
            presenter.insertPicture(
                    getActivity().getContentResolver().openInputStream(uri));

            call(imageChooser, AlertDialog::dismiss);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE
                && grantResults.length > 0 & grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadPicture(currentPictureUri);
        } else {
            ToastUtils.showShortTimeToast(R.string.toast_permission_denied_read_storage);
        }
    }

    @Override
    public void onLoginStateInvalid() {
        LoginPromptFragment.show(getActivity());
    }

    /**
     * 侦听编辑完成的事件。
     * @param listener
     */
    public void setOnCompleteListener(OnCompleteListener listener) {
        onCompleteListener = listener;
    }

    /**
     * 直接插入HTML代码。
     * @param html
     */
    public void insertHtml(String html) {
        editor.insertHtml(html);
    }

    /**
     * 获取编辑器的内容。
     * @return
     */
    public String getHtml() {
        return editor.getHtml();
    }

    @Override
    public void onLinkInvalid() {
        ToastUtils.showShortTimeToast(R.string.toast_url_invalid);
    }

    @Override
    public void insertLink(String name, String url) {
        editor.insertLink(url, name);

        call(linkDialog, AlertDialog::dismiss);
    }

    @Override
    public void setThumbnails(List<Thumbnail> data) {
        adapter.setData(data);
    }

    @Override
    public void onPictureUrlInvalid() {
        ToastUtils.showShortTimeToast(R.string.toast_image_url_invalid);
    }

    @Override
    public void insertPicture(String url, InputStream data) {
        imageMap.put(url, data);

        editor.insertImage(url, null);
    }

    @Override
    public void onThumbnailsChanged(boolean empty) {
        images.setVisibility(empty ? GONE : VISIBLE);
    }

    @Override
    public void onThumbnailRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void updateThumbnail(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onThumbnailAppended() {
        adapter.notifyDataAppended();
    }

    @Override
    public void onOutOfMemoryError() {
        ToastUtils.showShortTimeToast(R.string.toast_not_enough_memory);
    }

    class ViewHolder extends SimpleAdapter.ItemViewHolder<Thumbnail> {
        @Bind(R.id.image_thumbnail) ImageView thumbnail;
        @Bind(R.id.btn_remove) ImageButton remove;
        @Bind(R.id.loading) ProgressBar loading;
        @Bind(R.id.error) ImageView error;

        public ViewHolder(ViewGroup parent) {
            super(getContext(), R.layout.item_thumbnail, parent);

            itemView.setOnClickListener(v -> onClick());
            remove.setOnClickListener(v -> presenter.removePicture(data));
        }

        private void onClick() {
            switch (data.state) {
                case Thumbnail.FAILED:
                    popupRetryDialog();
                    break;
                case Thumbnail.SUCCESS:
                    presenter.onInsertPicture(data);
                    break;
                case Thumbnail.LOADING:
                    break;
            }
        }

        private void popupRetryDialog() {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.title_retry)
                    .setPositiveButton(R.string.btn_retry,
                            (d, w) -> presenter.reloadPicture(data))
                    .setNegativeButton(R.string.btn_cancel, null)
                    .show();
        }

        @Override
        protected void onBind(Thumbnail data, int position) {
            thumbnail.setImageBitmap(data.bitmap);
            loading.setVisibility(data.state == LOADING ? VISIBLE : GONE);
            error.setVisibility(data.state == FAILED ? VISIBLE : GONE);
        }
    }

    public interface OnCompleteListener {
        void onComplete(String content);
    }
}
