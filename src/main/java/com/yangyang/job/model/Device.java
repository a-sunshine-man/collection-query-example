package com.yangyang.job.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Device {
    /**
     * id
     */
    private Long devId;
    /**
     * name
     */
    private String devName;
    /**
     * secret
     */
    private Integer secret;
    /**
     * 是否激活
     */
    private Boolean active;
    /**
     * 最后上线时间
     */
    private LocalDateTime activeTime;

    public Device(Long devId, String devName, Integer secret, Boolean active, LocalDateTime activeTime) {
        this.devId = devId;
        this.devName = devName;
        this.secret = secret;
        this.active = active;
        this.activeTime = activeTime;
    }

    public static List<Device> mockDataList(Integer count){
        if (count == null) count = 100;
        List<Device> devices = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            boolean active = false;
            if (i % 2 == 0) active = true;
            devices.add(new Device((long) i, "name-" + i, i * 8 + 2, active, LocalDateTime.now().plusHours(i)));
        }
        return devices;
    }
}
