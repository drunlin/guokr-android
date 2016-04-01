package com.github.drunlin.guokr.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.Button;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.ArticleType;
import com.github.drunlin.guokr.util.ViewUtils;

import org.apmem.tools.layouts.FlowLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.drunlin.guokr.util.JavaUtil.call;
import static com.github.drunlin.guokr.util.JavaUtil.foreach;

/**
 * 能够自动换行显示主题站标签。
 *
 * @author drunlin@outlook.com
 */
public class ArticleLabelsView extends FlowLayout {
    /**标签颜色的映射表。*/
    private static Map<String, Integer> colorMap;
    /**没有指定标签颜色时的颜色。*/
    private static int commonColor;

    private OnLabelClickListener onLabelClickListener;

    public ArticleLabelsView(Context context) {
        this(context, null);
    }

    public ArticleLabelsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        if (isInEditMode()) {
            addView(getLabel(new ArticleType(ArticleType.CHANNEL, "hot|热点")));
            addView(getLabel(new ArticleType(ArticleType.CHANNEL, "review|评论")));
            addView(getLabel(new ArticleType(ArticleType.SUBJECT, "math|数学")));
        }
    }

    private void putLabelColor(String key, int resId) {
        colorMap.put(key, ContextCompat.getColor(getContext(), resId));
    }

    private void initLabelColors() {
        colorMap = new HashMap<>();
        //只有channel下的标签才有特殊颜色
        putLabelColor("fact", R.color.labelTruth);
        putLabelColor("visual", R.color.labelVisual);
        putLabelColor("interview", R.color.labelSpecial);
        putLabelColor("frontier", R.color.labelForward);
        putLabelColor("brief", R.color.labelRead);
        putLabelColor("hot", R.color.labelHot);
        putLabelColor("review", R.color.labelComment);

        commonColor = ContextCompat.getColor(getContext(), R.color.labelCommon);
    }

    private int getLabelColor(String key) {
        if (colorMap == null) {
            initLabelColors();
        }
        if (colorMap.containsKey(key)) {
            return colorMap.get(key);
        }
        return commonColor;
    }

    /**
     * 返回一个有文字和颜色的标签按钮。
     * @param type
     * @return
     */
    private Button getLabel(ArticleType type) {
        Button label = (Button) ViewUtils.inflate(getContext(), R.layout.btn_article_label, this);
        label.setText(type.name);
        setColor(label.getBackground(), getLabelColor(type.key));

        if (!isInEditMode()) {
            label.setOnClickListener(v -> call(onLabelClickListener, l -> l.onClick(type)));
        }

        return label;
    }

    private void setColor(Drawable drawable, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(color);
        } else {
            DrawableCompat.setTint(DrawableCompat.wrap(drawable), color);
        }
    }

    /**
     * 设置需要显示的标签。
     * @param labels
     */
    public void setLabels(List<ArticleType> labels) {
        removeAllViews();

        foreach(labels, type -> addView(getLabel(type)));
    }

    /**
     * 侦听标签点击事件。
     * @param listener
     */
    public void setOnLabelClickListener(OnLabelClickListener listener) {
        onLabelClickListener = listener;
    }

    /**
     * 标签点击事件。
     */
    public interface OnLabelClickListener {
        void onClick(ArticleType type);
    }
}
