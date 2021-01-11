package com.linaverde.fishingapp.interfaces;

public interface FishListChangeActionListener {
    void fishWeighChanged(int pos, int weight);
    void fishTimeChanged(int pos, String date, String time);
    void selectionChanged(int pos, String fishId);
}
