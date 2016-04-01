package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.Thumbnail;
import com.github.drunlin.guokr.model.EditorModel;
import com.github.drunlin.guokr.presenter.EditorPresenter;
import com.github.drunlin.guokr.util.UrlUtil;
import com.github.drunlin.guokr.view.EditorView;

import java.io.InputStream;

import javax.inject.Inject;

/**
 * 用于发帖或回复的编辑器，主要实现对插入图片的控制。
 *
 * @author drunlin@outlook.com
 */
public class EditorPresenterImpl
        extends LoginNeededPresenterBase<EditorView> implements EditorPresenter {

    @Inject EditorModel model;

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        bind(model.thumbnailRemoved(), view::onThumbnailRemoved);
        bind(model.thumbnailAppended(), this::onThumbnailAppended);
        bind(model.thumbnailsChanged(), view::updateThumbnail);
        bind(model.outOfMemoryError(), view::onOutOfMemoryError);

        view.setThumbnails(model.getThumbnails());
    }

    private void onThumbnailAppended() {
        view.onThumbnailAppended();

        checkThumbnailsCount();
    }

    private void checkThumbnailsCount() {
        switch (model.getThumbnails().size()) {
            case 0:
                view.onThumbnailsChanged(true);
                break;
            case 1:
                view.onThumbnailsChanged(false);
        }
    }

    @Override
    public void onInsertPicture(String url) {
        if (UrlUtil.validateUrl(url)) {
            view.insertPicture(url);
        } else {
            view.onPictureUrlInvalid();
        }
    }

    @Override
    public void insertPicture(String url) {
        if (UrlUtil.validateUrl(url)) {
            model.downloadImage(url);

            view.onPictureUrlValid();
        } else {
            view.onPictureUrlInvalid();
        }
    }

    @Override
    public void insertPicture(InputStream stream) {
        model.uploadImage(stream);
    }

    @Override
    public void onInsertLink(String name, String url) {
        if (UrlUtil.validateUrl(url)) {
            view.insertLink(name, url);
        } else {
            view.onLinkInvalid();
        }
    }

    @Override
    public void onInsertPicture(Thumbnail thumbnail) {
        view.insertPicture(thumbnail.url, model.getImage(thumbnail.url));
    }

    @Override
    public void reloadPicture(Thumbnail thumbnail) {
        model.reloadImage(thumbnail);
    }

    @Override
    public void removePicture(Thumbnail thumbnail) {
        model.deleteImage(thumbnail);

        checkThumbnailsCount();
    }

    @Override
    public void onViewDestroyed() {
        model.purge();
    }
}
