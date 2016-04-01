package com.github.drunlin.guokr.bean;

/**
 * 服务器的返回码。
 *
 * @author drunlin@outlook.com
 */
public class ResponseCode {
    //这两个为自定义的
    public static final int OK = 1;
    public static final int ERROR = 2;

    public static final int TOKEN_INVALID = 200004;
    public static final int ALREADY_LIKED = 240004;
//    public static final int ALREADY_THANKED = 242033;
//    public static final int ALREADY_BURIED = 242013;
}
