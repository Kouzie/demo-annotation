package com.demo.app.dto;

import com.demo.annotation.MavBuilder;
import com.demo.annotation.MavFieldInfo;
import io.dronefleet.mavlink.annotations.MavlinkMessageInfo;

@MavlinkMessageInfo(
        id = 5,
        crc = 200,
        description = "Flight Controller Information"
)
@MavBuilder
public class Ack {
    @MavFieldInfo(position = 1, unitSize = 2, description = "MAVLINK seqence of command")
    private int seq;
    @MavFieldInfo(position = 2, unitSize = 1, description = "message id of command")
    private int msgid;
}