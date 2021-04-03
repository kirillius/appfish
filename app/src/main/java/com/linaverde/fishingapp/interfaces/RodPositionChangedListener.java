package com.linaverde.fishingapp.interfaces;

public interface RodPositionChangedListener {
    void rodPositionChanged(int rodId, String landmark, double distance);
}
