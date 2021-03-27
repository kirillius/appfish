package com.linaverde.fishingapp.models;

public class MapMark {

    private double distance;
    private int rodId;
    private String landmark;
    private boolean isInfo;

    public MapMark(){
        distance = -1;
        isInfo = false;
        landmark = null;
        rodId = 0;
    }

    public MapMark(double distance){
        this.distance = distance;
        this.isInfo = true;
        landmark = null;
        rodId = 0;
    }

    public MapMark(double distance, int rodId, String landmark){
        this.distance = distance;
        this.rodId = rodId;
        this.landmark = landmark;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public int getRodId() {
        return rodId;
    }

    public void setRodId(int rodId) {
        this.rodId = rodId;
    }

    public boolean isInfo() {
        return isInfo;
    }
}
