package com.linaverde.fishingapp.models;

import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimersCollection {

    private static TimersCollection instance;
    List<CountDownTimer> timers;

    private TimersCollection() {
        timers = new ArrayList<>();
    }

    public static TimersCollection getInstance() {
        if (instance == null) {
            instance = new TimersCollection();
        }
        return instance;
    }

    public void addTimer(CountDownTimer timer) {
        timers.add(timer);
    }

    public void cancelAll() {
        for (int i = 0; i < timers.size(); i++) {
            if (timers.get(i) != null)
                timers.get(i).cancel();
        }
        timers.clear();
    }

    public void clearNull(){
        timers.removeAll(Collections.singleton(null));
    }
}
