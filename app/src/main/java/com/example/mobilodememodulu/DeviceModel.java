package com.example.mobilodememodulu;

public class DeviceModel {

    private String deviceId;
    private Long status;

    public DeviceModel(String deviceId, Long status) {
        this.deviceId = deviceId;
        this.status = status;
    }


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public DeviceModel(){

    }

}
