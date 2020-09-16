package com.yangyang.example.helper;

import com.yangyang.example.entity.Device;
import com.yangyang.example.enums.WhereConnEnum;
import com.yangyang.example.model.OrderWrapper;
import com.yangyang.example.model.WhereWrapper;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Getter
public class CollectionCustomQueryHelper {
    private static final String COMPARE_TO = "compareTo";
    private List<Device> dataList;
    public CollectionCustomQueryHelper(List<Device> dataList){
        this.dataList = dataList;
    }

    public List<Device> query(List<WhereWrapper> wheres, List<OrderWrapper> orders, Long maxSize) {
        if (dataList == null || dataList.isEmpty()) {
            return Collections.emptyList();
        }
        //获取当前成员class对象，流对象
        Class<? extends Device> elClazz = dataList.get(0).getClass();
        Stream<Device> stream = dataList.stream();

        //筛选
        if (wheres != null && !wheres.isEmpty()) {
            stream = stream.filter(e -> getFilterResult(wheres, elClazz, e));
        }
        //排序
        if (orders != null && !orders.isEmpty()) {
            Comparator<Device> comparing = null;
            for (int i = 0; i < orders.size(); i++) {
                OrderWrapper order = orders.get(i);
                Comparator<Device> tempComparing = getComparing(order);
                if (i == 0) {
                    comparing = tempComparing;
                    continue;
                }
                comparing = comparing.thenComparing(tempComparing);
            }
            stream = stream.sorted(comparing);
        }
        //设置最大返回结果数
        if (maxSize != null) {
            stream = stream.limit(maxSize);
        }
        return stream.collect(Collectors.toList());
    }

    private boolean getFilterResult(List<WhereWrapper> wheres, Class<? extends Device> elClazz, Device e) {
        boolean resultFlag = false;
        for (int i = 0; i < wheres.size(); i++) {
            WhereWrapper where = wheres.get(i);
            Field elClazzField = getField(elClazz,where.getFieldName());
            if (elClazzField == null) {
                continue;
            }
            boolean temp;
            try {
                temp = elClazzField.get(e).equals(where.getFieldVal());
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                continue;
            }
            if (i == 0) {
                resultFlag = temp;
            }else if (where.getConnectionType() == null
                    || where.getConnectionType() == WhereConnEnum.AND){
                resultFlag = resultFlag && temp;
            }else if (where.getConnectionType() == WhereConnEnum.OR){
                resultFlag = resultFlag || temp;
            }
        }
        return resultFlag;
    }

    private Comparator<Device> getComparing(OrderWrapper order) {
        return (o1, o2) -> {
                        Field field1 = getField(o1.getClass(), order.getFieldName());
                        Field field2 = getField(o2.getClass(), order.getFieldName());
                        if (field1 == null || field2 == null){
                            return 0;
                        }
                        Object valO1 = getVal(field1,o1);
                        Object valO2 = getVal(field1, o2);
                        if (valO1 == null || valO2 == null){
                            return 0;
                        }
                        if (order.getCondition() == OrderWrapper.Condition.ASC){
                            return compareTo(valO1,valO2);
                        }else if (order.getCondition() == OrderWrapper.Condition.DESC){
                            return compareTo(valO2,valO1);
                        }
                        return 0;
                    };
    }
    private int compareTo(Object o1, Object o2) {
        Method method;
        try {
            method = o1.getClass().getMethod(COMPARE_TO, o1.getClass());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return 0;
        }
        try {
            return (Integer) method.invoke(o1, o2);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private Object getVal(Field field, Device o) {
        try {
            return field.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Field getField(Class<? extends Device> elClazz, String fieldName) {
        Field elClazzField;
        try {
            elClazzField = elClazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
        elClazzField.setAccessible(true);
        return elClazzField;
    }
}
