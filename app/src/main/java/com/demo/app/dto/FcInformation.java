package com.demo.app.dto;

import io.dronefleet.mavlink.annotations.MavlinkFieldInfo;
import io.dronefleet.mavlink.annotations.MavlinkMessageBuilder;
import io.dronefleet.mavlink.annotations.MavlinkMessageInfo;

/**
 * Flight Controller Information.
 */
@MavlinkMessageInfo(
        id = 180,
        crc = 200,
        description = "Flight Controller Information"
)
public final class FcInformation {
    private final String manufacturer;
    private final String model;
    private final String fcSerialNum;
    private final String fcFirmwareVer;

    public String manufacturer() {
        return this.manufacturer;
    }

    public String model() {
        return this.model;
    }

    public String fcSerialNum() {
        return this.fcSerialNum;
    }

    public String fcFirmwareVer() {
        return this.fcFirmwareVer;
    }

    public FcInformation(String manufacturer, String model, String fcSerialNum, String fcFirmwareVer) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.fcSerialNum = fcSerialNum;
        this.fcFirmwareVer = fcFirmwareVer;
    }

    /**
     * Returns a builder instance for this message.
     */
    @MavlinkMessageBuilder
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String manufacturer;
        private String model;
        private String fcSerialNum;
        private String fcFirmwareVer;


        @MavlinkFieldInfo(
                position = 1,
                unitSize = 1,
                arraySize = 30,
                description = "manufacturer of FC"
        )
        public final Builder manufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        @MavlinkFieldInfo(
                position = 2,
                unitSize = 1,
                arraySize = 30,
                description = "manufacturer of FC"
        )
        public final Builder model(String model) {
            this.model = model;
            return this;
        }

        @MavlinkFieldInfo(
                position = 3,
                unitSize = 1,
                arraySize = 24,
                description = "serial number of FC"
        )
        public final Builder fcSerialNum(String fcSerialNum) {
            this.fcSerialNum = fcSerialNum;
            return this;
        }

        @MavlinkFieldInfo(
                position = 4,
                unitSize = 1,
                arraySize = 30,
                description = "firmware version of FC"
        )
        public final Builder fcFirmwareVer(String fcFirmwareVer) {
            this.fcFirmwareVer = fcFirmwareVer;
            return this;
        }

        public final FcInformation build() {
            return new FcInformation(manufacturer, model, fcSerialNum, fcFirmwareVer);
        }
    }
}
