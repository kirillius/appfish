package com.linaverde.fishingapp.interfaces;

public interface ViolationChangedRequestListener {
    void violationChangedRequest(String stageId, String teamId, String violation, int sector);
}
