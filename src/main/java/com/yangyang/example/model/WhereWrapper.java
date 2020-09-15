package com.yangyang.example.model;

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
     * 过滤条件
     */
    private Condition condition;

    public enum Condition{
        EQ,
        NE
    }
}
