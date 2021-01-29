package com.timi.modules.user.constant;
public enum Enabled {
    YES(1),NO(0);

    private Integer value;

    Enabled(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }}
