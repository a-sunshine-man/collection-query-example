package com.yangyang.example.helper;

import com.yangyang.example.entity.Device;
import com.yangyang.example.enums.WhereConnEnum;
import com.yangyang.example.model.OrderWrapper;
import com.yangyang.example.model.WhereWrapper;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * 单元测试代码
 * 结论：
 * 1.覆盖了常规流程，但未覆盖异常输入流程
 * 2.因未预处理查询条件（提前获取对应的Field/Method），可能数据量上来后造成性能问题，这是个优化点。
 */
public class CollectionCustomQueryHelperTest {
    private CollectionCustomQueryHelper queryHelper;
    private static String DEV_NAME = "name-3";

    /**
     * 数据准备：：helper初始化并注入数据
     */
    @Before
    public void mockData(){
        //mock 数据条目
        int count = 40000;
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
    public void queryMaxSize() {
        long maxSize = 10L;
        List<Device> list = queryHelper.query(null, null, maxSize);
        assertNotNull(list);
        assertTrue(list.size() == maxSize);
    }
    /**
     * 测试单个过滤条件
     */
    @Test
    public void queryWhereBySingleField(){
        boolean fieldVal = true;
        List<WhereWrapper> whereWrapper = Collections.singletonList(WhereWrapper.build("active", fieldVal));
        List<Device> list = queryHelper.query(whereWrapper, null, null);
        assertNotNull(list);
        assertTrue(list.size() > 0);
        //判断期望的都为true
        list.forEach(e-> assertEquals(fieldVal,e.getActive()));
        //剩下的都为false
        queryHelper.getDataList().parallelStream()
                .filter(e->!list.contains(e))
                .forEach(e-> assertNotEquals(fieldVal,e.getActive()));
    }
    /**
     * 测试多个过滤条件
     */
    @Test
    public void queryWhereByMoreField(){
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
        queryHelper.getDataList().parallelStream()
                .filter(e->!list.contains(e))
                .forEach(e-> assertFalse(fieldVal == e.getActive() && DEV_NAME.equals(e.getDevName())));
    }
    private void checkSortResult(List<Device> list, List<Device> listSource) {
        //返回的数据符合输入的条件
        assertNotNull(list);
        assertTrue(list.size() > 0);
        //与正常排序后的结果比较
        for (int i = 0; i < listSource.size(); i++) {
            Device devResult = list.get(i);
            Device devSource = listSource.get(i);
            assertEquals(devSource, devResult);
        }
    }
    /**
     * 测试单个排序功能-字符串String类型
     */
    @Test
    public void querySortBySingletonString(){
        OrderWrapper wrapper = OrderWrapper.build("devName", OrderWrapper.Condition.DESC);
        List<OrderWrapper> orders = Collections.singletonList(wrapper);
        //desc
        List<Device> list = queryHelper.query(null, orders, null);
        //对比正常排序后的结果
        List<Device> listSource = new ArrayList<>(queryHelper.getDataList());
        listSource.sort(Comparator.comparing(Device::getDevName).reversed());
        checkSortResult(list, listSource);
        //asc
        wrapper.setCondition(OrderWrapper.Condition.ASC);
        list = queryHelper.query(null, orders, null);
        //对比正常排序后的结果
        listSource = new ArrayList<>(queryHelper.getDataList());
        listSource.sort(Comparator.comparing(Device::getDevName));
        checkSortResult(list, listSource);
    }
    /**
     * 测试单个排序功能-number
     */
    @Test
    public void querySortBySingletonNumber(){
        OrderWrapper wrapper = OrderWrapper.build("devId", OrderWrapper.Condition.DESC);
        List<OrderWrapper> orders = Collections.singletonList(wrapper);
        //desc
        List<Device> list = queryHelper.query(null, orders, null);
        //对比正常排序后的结果
        List<Device> listSource = new ArrayList<>(queryHelper.getDataList());
        listSource.sort(Comparator.comparing(Device::getSecret).reversed());
        checkSortResult(list, listSource);
        //asc
        wrapper.setCondition(OrderWrapper.Condition.ASC);
        list = queryHelper.query(null, orders, null);
        //对比正常排序后的结果
        listSource = new ArrayList<>(queryHelper.getDataList());
        listSource.sort(Comparator.comparing(Device::getSecret));
        checkSortResult(list, listSource);
    }
    /**
     * 测试单个排序功能-boolean
     */
    @Test
    public void querySortAndSingletonBoolean(){
        OrderWrapper wrapper = OrderWrapper.build("active", OrderWrapper.Condition.ASC);
        //asc
        List<OrderWrapper> orders = Collections.singletonList(wrapper);
        List<Device> list = queryHelper.query(null, orders, null);
        //对比正常排序后的结果
        List<Device> listSource = new ArrayList<>(queryHelper.getDataList());
        listSource.sort(Comparator.comparing(Device::getActive));
        checkSortResult(list, listSource);
        //desc
        wrapper.setCondition(OrderWrapper.Condition.DESC);
        list = queryHelper.query(null, orders, null);
        //对比正常排序后的结果
        listSource = new ArrayList<>(queryHelper.getDataList());
        listSource.sort(Comparator.comparing(Device::getActive).reversed());
        checkSortResult(list, listSource);

    }
    /**
     * 测试多级排序
     */
    @Test
    public void querySortByMoreField() {
        //按devName,devId字段排序
        OrderWrapper orderByFiled1Desc = OrderWrapper.build("devName", OrderWrapper.Condition.DESC);
        OrderWrapper orderByFiled2Asc = OrderWrapper.build("devId", OrderWrapper.Condition.ASC);
        List<OrderWrapper> orders = Arrays.asList(orderByFiled1Desc,orderByFiled2Asc);

        List<Device> list = queryHelper.query(null, orders, null);
        //对比正常排序后的结果
        List<Device> listSource = new ArrayList<>(queryHelper.getDataList());
        listSource.sort(Comparator.comparing(Device::getDevName).reversed()
                .thenComparing(Device::getDevId));
        checkSortResult(list, listSource);

        //按secret,devId字段排序
        orderByFiled1Desc.setFieldName("secret");
        orderByFiled2Asc.setFieldName("devId");
        //对比正常排序后的结果
        listSource = new ArrayList<>(queryHelper.getDataList());
        listSource.sort(Comparator.comparing(Device::getDevName).reversed()
                .thenComparing(Device::getDevId));
        checkSortResult(list, listSource);
    }

}
