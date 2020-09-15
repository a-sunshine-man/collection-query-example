package com.yangyang.example.model;

import com.yangyang.example.enums.WhereConnEnum;
import lombok.Data;

/**
 * 过滤条件
 *
 * @author qiany@isyscore.com
 * @date 2020-09-15
 */
@Data
public class WhereWrapper {
    /**
     * 条件字段名
     */
    private String fieldName;
    /**
     * 筛选字段值
     */
    private Object fieldVal;

    /**
     * 多个条件查询时指定查询的方式，默认：与
     */
    private WhereConnEnum connectionType;
    private WhereWrapper(){ }

    public static WhereWrapper build(String fieldName, Object fieldVal) {
        WhereWrapper wrapper = new WhereWrapper();
        wrapper.fieldName = fieldName;
        wrapper.fieldVal = fieldVal;
        return wrapper;
    }

    public static WhereWrapper build(String fieldName, Object fieldVal, WhereConnEnum connectionType) {
        WhereWrapper wrapper = new WhereWrapper();
        wrapper.fieldName = fieldName;
        wrapper.fieldVal = fieldVal;
        wrapper.setConnectionType(connectionType);
        return wrapper;
    }
}
