package com.linaverde.fishingapp.interfaces;

public interface RodsSettingsChangeListener {
    void paramChanged(String paramId, String value, boolean addInfo);
    void openMap(RodPositionChangedListener listener);
}
