package com.github.drunlin.guokr.bean;

/**
 * 主题站文章类型。
 *
 * @author drunlin@outlook.com
 */
public class ArticleType {
    public static final int CHANNEL = 0;
    public static final int SUBJECT = 1;

    public String name;
    /**向服务器请求时的值。*/
    public String key;
    /**类型的大分类。*/
    public transient int type;

    public ArticleType() {}

    public ArticleType(int type, String info) {
        String[] data = info.split("\\|");
        key = data[0];
        name = data[1];
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof ArticleType) {
            return key.equals(((ArticleType) o).key);
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
