package com.linaverde.fishingapp.interfaces;

public interface FishChangedRequestListener {
    void fishChangedRequest(String stageId, String teamId, String pin,String fish, int sector);
}
