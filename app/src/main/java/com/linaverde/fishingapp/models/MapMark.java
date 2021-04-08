package com.linaverde.fishingapp.models;

public class MapMark {

    private double distance;
    private int rodId;
    private int spodId;
    private String landmark;
    private boolean isInfo;



    public MapMark(double distance) {
        this.distance = distance;
        this.isInfo = true;
        this.landmark = null;
        this.rodId = 0;
        this.spodId = 0;
    }

    public MapMark(String landmark, double distance) {
        this.distance = distance;
        this.isInfo = false;
        this.landmark = landmark;
        this.rodId = 0;
        this.spodId = 0;
    }

    public MapMark(double distance, int rodId, String landmark) {
        this.distance = distance;
        this.rodId = rodId;
        this.landmark = landmark;
        this.isInfo = false;
        this.spodId = 0;
    }

    public MapMark(double distance, int rodId, String landmark, int spodId) {
        this.distance = distance;
        this.rodId = rodId;
        this.landmark = landmark;
        this.isInfo = false;
        this.spodId = spodId;
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

    public int getSpodId() {
        return spodId;
    }

    public void setSpodId(int spodId) {
        this.spodId = spodId;
    }
}
