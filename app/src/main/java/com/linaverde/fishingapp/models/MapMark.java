package com.linaverde.fishingapp.models;

public class MapMark {

    private double value;
    private final boolean isInfo;

    public MapMark(){
        value = -1;
        isInfo = false;
    }

    public MapMark(double  value, boolean info){
        this.value = value;
        this.isInfo = info;
    }

    public double  getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isInfo() {
        return isInfo;
    }
}
