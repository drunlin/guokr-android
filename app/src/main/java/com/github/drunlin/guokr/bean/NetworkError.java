package com.github.drunlin.guokr.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 果壳网返回的错误码。
 *
 * @author drunlin@outlook.com
 */
public class NetworkError {
    @SerializedName("error_code")
    public int errorCode;
}
