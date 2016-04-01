package com.github.drunlin.guokr.model.impl;

import android.content.res.Resources;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.ArticleType;
import com.github.drunlin.guokr.model.ArticleListModel;
import com.github.drunlin.guokr.model.ArticleModel;
import com.github.drunlin.guokr.model.MinisiteModel;
import com.github.drunlin.guokr.model.Model;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.module.tool.ReferenceMap;

import java.util.ArrayList;
import java.util.List;

import static com.github.drunlin.guokr.util.JavaUtil.find;

/**
 * 科学人下的文章数据。
 *
 * @author drunlin@outlook.com
 */
public class MinisiteModelImpl extends Model implements MinisiteModel {
    /**所有的文章类型。*/
    private static List<ArticleType> types;

    private final ReferenceMap<ArticleType, ArticleListModel> listModeMap = new ReferenceMap<>();
    private final ReferenceMap<Integer, ArticleModel> articleModeMap = new ReferenceMap<>();

    public MinisiteModelImpl(Injector injector) {
        super(injector);
    }

    @Override
    public ArticleListModel getArticles(ArticleType type) {
        if (getTypes().contains(type)) {
            return listModeMap.get(type, () -> new ArticleListModelImpl(injector, type));
        } else {
            return null;
        }
    }

    @Override
    public ArticleListModel getArticles(String key) {
        return getArticles(find(getTypes(), v -> v.key.equals(key)));
    }

    @Override
    public List<ArticleType> getTypes() {
        if (types == null) {
            Resources res = App.getContext().getResources();

            types = new ArrayList<>();
            //单独一个全部
            types.add(new ArticleType(ArticleType.CHANNEL, res.getString(R.string.article_all)));
            //channels
            for (String s : res.getStringArray(R.array.article_channels)) {
                types.add(new ArticleType(ArticleType.CHANNEL, s));
            }
            //subjects
            for (String s : res.getStringArray(R.array.article_subjects)) {
                types.add(new ArticleType(ArticleType.SUBJECT, s));
            }
        }
        return types;
    }

    @Override
    public ArticleModel getArticle(int articleId) {
        return articleModeMap.get(articleId, () -> new ArticleModelImpl(injector, articleId));
    }
}
