package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.ArticleType;

import java.util.List;

/**
 * 主题站的界面，实现对所有类型的文章列表的管理。
 *
 * @author drunlin@outlook.com
 */
public interface MinisiteView {
    /**
     * 所有的文章类型。
     * @param types
     */
    void setTypes(List<ArticleType> types);

    /**
     * 没有此类类型错误。
     */
    void onNoSuchTypeError();

    /**
     * 显示某个分类。
     * @param position
     */
    void displayAt(int position);
}
