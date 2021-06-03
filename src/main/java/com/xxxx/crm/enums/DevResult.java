package com.xxxx.crm.enums;

public enum DevResult {
    UNDEV(0),DEVING(1),DEV_SUCCESS(2),DEV_FAILED(3);

    private Integer status;

    DevResult(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
