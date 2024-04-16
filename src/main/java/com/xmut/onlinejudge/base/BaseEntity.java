package com.xmut.onlinejudge.base;

import com.mybatisflex.annotation.Column;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础实体类
 *
 * @author bill
 */

@Data
public class BaseEntity implements Serializable {

    @Column(ignore = true)
    private Object etc;

    @SuppressWarnings("unchecked")
    public void put(String key, Object value) {
        if (etc == null) {
            etc = new HashMap<String, Object>();
        }
        ((Map<String, Object>) etc).put(key, value);
    }

}
