package com.linaverde.fishingapp.models;

public class MapMark {

    private int value;

    public MapMark(){
        value = -1;
    }

    public MapMark(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
