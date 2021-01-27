package com.linaverde.fishingapp.services;

import android.content.Context;
import android.content.Intent;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.activities.AuthActivity;

public class NavigationHelper {

    public static void onMenuItemClicked(Context context, int id, DrawerLayout drawer) {
        if (id == R.id.nav_exit) {
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
