package com.github.drunlin.guokr.widget;

import android.view.ViewGroup.LayoutParams;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.test.WidgetTestCase;

import org.junit.Test;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.github.drunlin.guokr.test.util.TestUtils.getBitmapData;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * @author drunlin@outlook.com
 */
public class BitmapImageViewTest extends WidgetTestCase<BitmapImageView> {
    @Override
    protected void initOnUiThread() throws Throwable {
        addToScene(new BitmapImageView(activity),
                new LayoutParams(MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private void setImageData(int resId) throws Throwable {
        runOnUiThread(() -> view.setImageData(getBitmapData(resId)));
        waitForIdleSync();
        assertThat(view.getHeight(), greaterThan(0));
        pauseToPreview();
    }

    @Test
    public void setImageData() throws Throwable {
        setImageData(R.drawable.img_gif);
        setImageData(R.drawable.img_png);
        setImageData(R.drawable.img_jpg);
    }
}
