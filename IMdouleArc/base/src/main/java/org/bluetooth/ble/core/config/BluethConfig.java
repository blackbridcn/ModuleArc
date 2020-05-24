package org.bluetooth.ble.core.config;


import org.bluetooth.ble.exception.BleException;
import org.bluetooth.ble.utils.ObjectHelp;

public final class BluethConfig {

    //蓝牙扫描超时默认为10s
    long scanTimeout = 5 * 100;
    String serviceUUID;
    String characterUUID;

    public BluethConfig() {
    }

    public BluethConfig(String serviceUUID, String characterUUID, long scanTimeout) {
        this.serviceUUID = serviceUUID;
        this.characterUUID = characterUUID;
        this.scanTimeout = scanTimeout;
    }

    public BluethConfig(Builder builder) {
        this.scanTimeout = builder.scanTimeout;
        this.characterUUID = builder.characterUUID;
        this.serviceUUID = builder.serviceUUID;
    }

    public long getScanTimeout() {
        return scanTimeout;
    }

    public String getServiceUUID() {
        return serviceUUID;
    }

    public String getCharacterUUID() {
        return characterUUID;
    }

    public void setScanTimeout(long scanTimeout) {
        this.scanTimeout = scanTimeout;
    }

    public void setServiceUUID(String serviceUUID) {
        this.serviceUUID = serviceUUID;
    }

    public void setCharacterUUID(String characterUUID) {
        this.characterUUID = characterUUID;
    }

    public static final class Builder {
        long scanTimeout;
        String serviceUUID;
        String characterUUID;
        Builder(long scanTimeout){
            this.scanTimeout = scanTimeout;
        }
        public Builder() {
            this(5 * 100);
        }


        public Builder setScanTimeout(long timeout) {
            this.scanTimeout = timeout;
            return this;
        }

        public Builder serviceUUID(String serviceUUID) {
            this.serviceUUID = serviceUUID;
            return this;
        }

        public Builder characterUUID(String characterUUID) {
            this.characterUUID = characterUUID;
            return this;
        }

        public BluethConfig build() {
            if (ObjectHelp.checkNotNullEmpty(serviceUUID, characterUUID))
                return new BluethConfig(this);
            else throw new BleException(" must set serviceUUID and characterUUID value !");
        }
    }


}
