package com.linaverde.fishingapp.interfaces;

public interface FishChangedRequestListener {
    void fishChangedRequest(String stageId, String teamId, String pin,String fish, int sector);
    void fishAdded(String dict, String pin, String teamId, String stageId, int sector);
    void fishChanged(String fish, String dict, String pin, String teamId, String stageId, int sector);
}
