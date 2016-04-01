package com.github.drunlin.guokr.model.impl;

import android.content.res.Resources;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Thumbnail;
import com.github.drunlin.guokr.test.ModelTestCase;
import com.github.drunlin.signals.impl.Signal1;

import org.junit.Test;

import okio.Buffer;

import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlEqualTo;
import static com.github.drunlin.guokr.test.util.Constants.IMAGE_URL;
import static com.github.drunlin.guokr.test.util.TestUtils.getBitmapData;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author drunlin@outlook.com
 */
public class EditorModelImplTest extends ModelTestCase<EditorModelImpl> {
    private Resources resources;

    @Override
    protected void init() throws Throwable {
        model = new EditorModelImpl(injector);

        resources = App.getContext().getResources();
    }

    protected void uploadImage() {
        stubFor(request -> true, urlContainers("http://www.guokr.com/apis/image.json"))
                .setBody(getText(R.raw.upload_image));

        model.thumbnailsChanged().add(index -> {
            Thumbnail thumbnail = model.getThumbnails().get(index);
            assertEquals(Thumbnail.SUCCESS, thumbnail.state);
            countDown();
        });
    }

    @Test
    public void uploadImage_byteArray() throws Exception {
        uploadImage();

        model.uploadImage(getBitmapData(R.drawable.img_gif));
        await();
    }

    @Test
    public void uploadImage_stream() throws Exception {
        uploadImage();

        //noinspection ResourceType
        model.uploadImage(resources.openRawResource(R.drawable.img_jpg));
        await();
    }

    @Test
    public void downloadImage() throws Exception {
        Buffer buffer = new Buffer();
        //noinspection ResourceType
        buffer.readFrom(resources.openRawResource(R.drawable.img_png));
        stubFor(urlEqualTo(IMAGE_URL)).setBody(buffer);

        model.thumbnailsChanged().add(v -> countDown());
        model.downloadImage(IMAGE_URL);
        await();
    }

    @Test
    public void thumbnailAppended() throws Exception {
        assertNotNull(model.thumbnailAppended());
    }

    @Test
    public void thumbnailChanged() throws Exception {
        assertNotNull(model.thumbnailsChanged());
    }

    @Test
    public void getThumbnails() throws Exception {
        //noinspection unchecked
        assertThat(model.getThumbnails(), anyOf(notNullValue(), empty()));

        uploadImage_byteArray();
        downloadImage();

        assertThat(model.getThumbnails(), allOf(notNullValue(), hasSize(2)));
    }

    @Test
    public void deleteImage() throws Exception {
        getThumbnails();

        final int index = 1;

        //noinspection unchecked
        Signal1.Listener<Integer> listener = mock(Signal1.Listener.class);
        model.thumbnailRemoved().add(listener);

        Thumbnail thumbnail = model.getThumbnails().get(index);
        model.deleteImage(model.getThumbnails().get(index));

        verify(listener).call(index);
        assertThat(model.getThumbnails(), not(hasItem(thumbnail)));
    }

    @Test
    public void getImage() throws Exception {
        downloadImage();

        assertNotNull(model.getImage(IMAGE_URL));
    }

    @Test
    public void outOfMemoryError() throws Exception {
        assertNotNull(model.outOfMemoryError());
    }

    @Test
    public void clear() throws Exception {
        model.purge();
    }
}
