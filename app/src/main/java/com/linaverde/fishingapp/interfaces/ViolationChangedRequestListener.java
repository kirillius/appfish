package com.linaverde.fishingapp.interfaces;

public interface ViolationChangedRequestListener {
    void violationChangedRequest(String stageId, String teamId, String violation, int sector);
    void violationAdded(String dict, String teamId, String stageId, int sector);
    void violationChanged(String foul, String dict, String teamId, String stageId, int sector);
}
