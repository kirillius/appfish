package com.linaverde.fishingapp.interfaces;

public interface FishChangedRequestListener {
    void fishChangedRequest(String stageId, String teamId, String pin, String pin2,String fish, int sector);
    void fishAdded(String dict, String pin, String pin2, String teamId, String stageId, int sector);
    void fishChanged(String fish, String dict, String pin, String pin2, String teamId, String stageId, int sector);
    void fishDeleted(String fishId, String stageId, String teamId, String pin, String pin2, int sector);
}
