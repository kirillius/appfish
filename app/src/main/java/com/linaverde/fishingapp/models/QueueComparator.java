package com.linaverde.fishingapp.models;

import java.util.Comparator;

public class QueueComparator implements Comparator<TeamsQueue> {

    @Override
    public int compare(TeamsQueue o1, TeamsQueue o2) {
        return o1.getQueue() - o2.getQueue();
    }
}