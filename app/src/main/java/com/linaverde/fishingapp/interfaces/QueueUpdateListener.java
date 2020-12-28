package com.linaverde.fishingapp.interfaces;

import com.linaverde.fishingapp.models.TeamsQueue;

public interface QueueUpdateListener {
    void update(TeamsQueue teams, String input);
}
