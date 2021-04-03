package com.linaverde.fishingapp.interfaces;

public interface RodsSettingsChangeListener {
    void paramChanged(String paramId, String value);
    void openMap(RodPositionChangedListener listener);
}
