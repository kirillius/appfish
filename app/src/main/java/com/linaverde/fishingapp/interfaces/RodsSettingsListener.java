package com.linaverde.fishingapp.interfaces;

import com.linaverde.fishingapp.fragments.MapFragment;

public interface RodsSettingsListener {
    void rodsDetailedReqired(String rodType, int rodID);
    void updateDetailedFragment(String rodType, int rodID);
    void sendRodsSettings(String rodType, int rodID, String newParams);
    void openMapFragment(MapFragment MapFragment, RodPositionChangedListener listener);

}
