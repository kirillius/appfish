package com.linaverde.fishingapp.interfaces;

public interface WeightingSelectedTeamClickListener {
    void fishClicked(String teamId, String stageId, String pin, int sector);
    void violationClicked(String teamId, String stageId, String pin, int sector);
}
