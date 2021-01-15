package com.linaverde.fishingapp.interfaces;

public interface ViolationListChangeListener {
    void selectionChanged(int pos, String foulId);
    void violationTimeChanged(int pos, String date, String time);
}
