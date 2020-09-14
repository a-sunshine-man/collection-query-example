package com.yangyang.job.helper;

import com.yangyang.job.model.Device;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CollectionCustomQueryHelper<T> {
    private List<T> dataList;

    public CollectionCustomQueryHelper(List<T> dataList){
        this.dataList = dataList;
    }

    public List<T> queryByWrapper(){
        if (dataList == null || dataList.isEmpty()){
            return Collections.emptyList();
        }

        return null;
    }

    public static void main(String[] args) {
        List<Device> list = Device.mockDataList(1000);

    }
}
