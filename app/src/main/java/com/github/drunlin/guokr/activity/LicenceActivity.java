package com.github.drunlin.guokr.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.drunlin.guokr.R;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 引用的第三方开源库的说明界面。
 *
 * @author drunlin@outlook.com
 */
public class LicenceActivity extends AppCompatActivity {
    @Bind(R.id.text) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_licence);
        ButterKnife.bind(this);

        try {
            InputStream stream = getResources().openRawResource(R.raw.licences);
            textView.setText(new String(ByteStreams.toByteArray(stream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
