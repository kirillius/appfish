package com.linaverde.fishingapp.models;

import java.util.Comparator;

public class SectorComparator implements Comparator<TeamsQueue> {

    @Override
    public int compare(TeamsQueue o1, TeamsQueue o2) {
        return o1.getSector() - o2.getSector();
    }
}