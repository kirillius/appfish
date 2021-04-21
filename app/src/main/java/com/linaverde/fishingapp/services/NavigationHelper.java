package com.linaverde.fishingapp.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.activities.AuthActivity;
import com.linaverde.fishingapp.activities.TeamStatisticsActivity;
import com.linaverde.fishingapp.activities.TeamsActivity;
import com.linaverde.fishingapp.activities.TournamentActivity;
import com.linaverde.fishingapp.models.UserInfo;

public class NavigationHelper {

    public static void onMenuItemClicked(Context context, int id, DrawerLayout drawer) {
        if (id == R.id.nav_main) {
            Intent intent = new Intent(context, TournamentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        } else if (id == R.id.nav_teams) {
            Intent intent = new Intent(context, TeamsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
//        } else if (id == R.id.nav_weather) {
//            Intent intent = new Intent(context, WeatherActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            context.startActivity(intent);
        } else if (id == R.id.nav_team_stats) {
            UserInfo userInfo = new UserInfo(context);
            Intent intent = new Intent(context, TeamStatisticsActivity.class);
            Bundle args = new Bundle();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            args.putString("matchId", userInfo.getMatchId());
            args.putString("teamId", userInfo.getTeamId());
            args.putString("matchName", userInfo.getMatchName());
            intent.putExtras(args);
            context.startActivity(intent);
        } else if (id == R.id.nav_exit) {
            UserInfo userInfo = new UserInfo(context);
            userInfo.clearUserInfo();
            Intent intent = new Intent(context, AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
    }
}
