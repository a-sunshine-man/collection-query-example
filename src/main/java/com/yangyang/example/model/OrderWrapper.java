package com.yangyang.example.model;

import lombok.Data;

/**
 * 排序规则
 *
 * @author qiany@isyscore.com
 * @date 2020-09-15
 */
@Data
public class OrderWrapper {
    /**
     * 条件字段名
     */
    private String fieldName;
    /**
     * 排序类别
     */
    private Condition condition;

    public enum Condition{
        ORDER_BY_ASC,
        ORDER_BY_DESC
    }
}
