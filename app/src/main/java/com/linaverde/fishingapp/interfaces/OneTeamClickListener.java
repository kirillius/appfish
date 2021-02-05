package com.linaverde.fishingapp.interfaces;

public interface OneTeamClickListener {
    void onViolationClicked(String teamId);
    void onStatisticsClicked(String teamId);
    void teamRegistered(String teamId, boolean register);
}
