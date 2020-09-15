package com.yangyang.example.helper;

import com.yangyang.example.entity.Device;
import com.yangyang.example.enums.WhereConnEnum;
import com.yangyang.example.model.WhereWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
public class CollectionCustomQueryHelperTest {
    private CollectionCustomQueryHelper queryHelper;
    private static String DEV_NAME = "name-3";

    /**
     * 数据准备：：helper初始化并注入数据
     */
    @Before
    public void mockData(){
        //mock 数据条目
        int count = 20;
        List<Device> devices = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            boolean active = false;
            String devName = "name-" + i;
            if (i % 2 == 0) active = true;
            if (i % 3 == 0) devName = DEV_NAME;
            devices.add(new Device((long) i, devName, i * 8 + 2, active));
        }
        queryHelper = new CollectionCustomQueryHelper(devices);
    }

    /**
     * 获取最大返回条目数测试
     */
    @Test
    public void queryByMaxSize() {
        long maxSize = 10L;
        List<Device> list = queryHelper.query(null, null, maxSize);
        assertNotNull(list);
        assertTrue(list.size() == maxSize);
    }
    /**
     * 测试单个过滤条件
     */
    @Test
    public void queryBySingleWhere(){
        boolean fieldVal = true;
        List<WhereWrapper> whereWrapper = Arrays.asList(WhereWrapper.build("active", fieldVal));
        List<Device> list = queryHelper.query(whereWrapper, null, null);
        assertNotNull(list);
        assertTrue(list.size() > 0);
        //判断期望的都为true
        list.forEach(e-> assertEquals(fieldVal,e.getActive()));
        //剩下的都为false
        queryHelper.getDataList().stream()
                .filter(e->!list.contains(e))
                .forEach(e-> assertNotEquals(fieldVal,e.getActive()));
    }
    /**
     * 测试多个过滤条件
     */
    @Test
    public void queryByMoreWhere(){
        boolean fieldVal = true;
        List<WhereWrapper> whereWrappers = Arrays.asList(
                WhereWrapper.build("active", fieldVal),
                WhereWrapper.build("devName",DEV_NAME, WhereConnEnum.AND)
        );
        List<Device> list = queryHelper.query(whereWrappers, null, null);
        //返回的数据符合输入的条件
        assertNotNull(list);
        assertTrue(list.size() > 0);
        list.forEach( e-> {
            assertEquals(fieldVal,e.getActive());
            assertEquals(DEV_NAME,e.getDevName());
        });
        //未返回的数据都不符合输入条件
        queryHelper.getDataList().stream()
                .filter(e->!list.contains(e))
                .forEach(e-> assertFalse(fieldVal == e.getActive() && DEV_NAME.equals(e.getDevName())));
    }
}
