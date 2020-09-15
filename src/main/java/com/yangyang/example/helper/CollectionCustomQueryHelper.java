package com.yangyang.example.helper;

import com.yangyang.example.entity.Device;
import com.yangyang.example.enums.WhereConnEnum;
import com.yangyang.example.model.OrderWrapper;
import com.yangyang.example.model.WhereWrapper;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Getter
public class CollectionCustomQueryHelper {
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
            stream = stream.filter(e -> {
                boolean resultFlag = false;
                for (int i = 0; i < wheres.size(); i++) {
                    WhereWrapper where = wheres.get(i);
                    boolean temp = compareToVal(elClazz, e, where);
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
            });
        }
        //排序
        if (orders != null && !orders.isEmpty()){
            //list.sort(Comparator.comparing(Device).reversed().thenComparing().);
        }
        //设置最大返回结果数
        if (maxSize != null) {
            stream = stream.limit(maxSize);
        }
        return stream.collect(Collectors.toList());
    }

    private boolean compareToVal(Class<? extends Device> elClazz,Object sourceObj, WhereWrapper where) {
        try {
            Field elClazzField = elClazz.getDeclaredField(where.getFieldName());
            elClazzField.setAccessible(true);
            return elClazzField.get(sourceObj).equals(where.getFieldVal());
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
