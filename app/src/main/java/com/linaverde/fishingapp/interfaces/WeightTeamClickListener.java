package com.linaverde.fishingapp.interfaces;

import com.linaverde.fishingapp.models.TeamsQueue;

public interface WeightTeamClickListener {
    void onTeamClicked(TeamsQueue selectedTeam, String stageId);
    void updateStages(boolean popStackBack);
}
