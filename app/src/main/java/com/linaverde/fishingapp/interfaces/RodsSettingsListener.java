package com.linaverde.fishingapp.interfaces;

import com.linaverde.fishingapp.fragments.MapFragment;

public interface RodsSettingsListener {
    void rodsDetailedReqired(String rodType, int rodID, boolean cast);
    void updateDetailedFragment(String rodType, int rodID, boolean cast);
    void sendRodsSettings(String rodType, int rodID, String newParams);
    void openMapFragment(MapFragment MapFragment, RodPositionChangedListener listener);

}
