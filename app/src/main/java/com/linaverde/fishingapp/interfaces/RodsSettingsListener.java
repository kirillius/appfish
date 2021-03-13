package com.linaverde.fishingapp.interfaces;

public interface RodsSettingsListener {
    void rodsDetailedReqired(String rodType, int rodID);
    void updateDetailedFragment(String rodType, int rodID);
    void sendRodsSettings(String rodType, int rodID, String newParams);
}
