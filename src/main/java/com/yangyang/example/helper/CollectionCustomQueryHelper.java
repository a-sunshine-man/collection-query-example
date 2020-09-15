package com.yangyang.example.helper;

import com.yangyang.example.entity.Device;
import com.yangyang.example.model.OrderWrapper;
import com.yangyang.example.model.WhereWrapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionCustomQueryHelper<T> {
    private List<T> dataList;

    public CollectionCustomQueryHelper(List<T> dataList){
        this.dataList = dataList;
    }

    public List<T> queryByWrapper(WhereWrapper whereWrapper, OrderWrapper orderWrapper, long maxSize){
        if (dataList == null || dataList.isEmpty()){
            return Collections.emptyList();
        }
        //实现最大返回结果数
        List<T> resultList = dataList.stream().limit(maxSize).collect(Collectors.toList());
        return resultList;
    }

    public static void main(String[] args) {
        List<Device> list = Device.mockDataList(1000);


        //list.sort(Comparator.comparing(Device).reversed().thenComparing().);
//        CollectionCustomQueryHelper helper = new CollectionCustomQueryHelper(list);
//        List query = helper.queryByWrapper(10);
//        System.out.println(query);
    }
}
