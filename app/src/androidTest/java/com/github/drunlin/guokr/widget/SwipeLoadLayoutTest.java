package com.github.drunlin.guokr.widget;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.test.WidgetTestCase;
import com.github.drunlin.guokr.util.ToastUtils;

import org.junit.Test;

/**
 * @author drunlin@outlook.com
 */
public class SwipeLoadLayoutTest extends WidgetTestCase<SwipeLoadLayout> {
    @Override
    protected void initOnUiThread() throws Throwable {
        addToScene(R.layout.test_swipe_load_layout);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(new TextView(activity)) {};
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView).setText("item" + position);
            }

            @Override
            public int getItemCount() {
                return 50;
            }
        });
    }

    @Test
    public void setOnLoadMoreListener() throws Throwable {
        view.setOnLoadMoreListener(() -> ToastUtils.showShortTimeToast("加载更多"));
        pauseToPreview();
    }
}
