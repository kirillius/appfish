package com.linaverde.fishingapp.models;

public class MapMark {

    private double distance;
    private int rodId;
    private int spodId;
    private String landmark;
    private boolean isInfo;
    private boolean cast;
    private String comment;
    private int depth;



    public MapMark(double distance) {
        this.distance = distance;
        this.isInfo = true;
        this.landmark = null;
        this.rodId = 0;
        this.spodId = 0;
        this.cast = false;
        this.comment = null;
        this.depth = 0;
    }

    public MapMark(String landmark, double distance) {
        this.distance = distance;
        this.isInfo = false;
        this.landmark = landmark;
        this.rodId = 0;
        this.spodId = 0;
        this.cast = false;
        this.comment = null;
        this.depth = 0;
    }

    public MapMark(double distance, int rodId, String landmark, String comment, boolean cast, int depth) {
        this.distance = distance;
        this.rodId = rodId;
        this.landmark = landmark;
        this.isInfo = false;
        this.spodId = 0;
        this.cast = cast;
        this.cast = false;
        this.comment = comment;
        this.depth = depth;
    }

    public MapMark(double distance, int rodId, String landmark, int spodId) {
        this.distance = distance;
        this.rodId = rodId;
        this.landmark = landmark;
        this.isInfo = false;
        this.spodId = spodId;
        this.cast = false;
        this.comment = null;
        this.depth = 0;
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

    public boolean isCast() {
        return cast;
    }

    public String getComment() {
        return comment;
    }

    public int getDepth() {
        return depth;
    }
}
