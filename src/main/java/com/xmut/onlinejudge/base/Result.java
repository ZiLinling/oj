package com.xmut.onlinejudge.base;


import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Data
public class Result<T> implements Serializable {

    private String error = null;

    private String message = null;

    private T data = null;
    private Object etc;

    public void put(String key, Object value) {
        if (etc == null) {
            etc = new HashMap<String, Object>();
        }
        ((Map<String, Object>) etc).put(key, value);
    }

    public void success(T data, String msg) {
        this.data = data;
        this.message = msg;
    }

    public void error(String msg) {
        this.error = "error";
        this.message = msg;
    }
}
