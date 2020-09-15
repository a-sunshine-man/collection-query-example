package com.yangyang.example.entity;

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

    public Device(Long devId, String devName, Integer secret, Boolean active) {
        this.devId = devId;
        this.devName = devName;
        this.secret = secret;
        this.active = active;
    }
}
