package com.github.drunlin.guokr.bean;

/**
 * 果壳网的果篮。
 *
 * @author drunlin@outlook.com
 */
public class Basket {
    public int id;
    public String title;
    public transient boolean added;

    /**
     * 果篮的类型。
     */
    public static class Category {
        public int id;
        public String name;

        @Override
        public String toString() {
            return name;
        }
    }
}
