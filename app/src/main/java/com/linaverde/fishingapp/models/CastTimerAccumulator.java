package com.linaverde.fishingapp.models;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;

import androidx.core.app.NotificationCompat;

import com.linaverde.fishingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CastTimerAccumulator {

    private static String CHANNEL_ID = "g_carp";
    private static CastTimerAccumulator instance;

    CountDownTimer[] timers;
    boolean timersAlreadyCreated;


    private CastTimerAccumulator() {
        timers = new CountDownTimer[4];
        for (int i = 0; i < 4; i++) {
            timers[i] = null;
        }
        timersAlreadyCreated = false;
    }

    public static CastTimerAccumulator getInstance() {
        if (instance == null) {
            instance = new CastTimerAccumulator();
        }
        return instance;
    }

    public boolean isTimersAlreadyCreated() {
        return timersAlreadyCreated;
    }

    public void createTimers(Context context, JSONArray events) throws JSONException {
        timersAlreadyCreated = true;
        for (int i = 0; i < events.length(); i++) {
            JSONObject object = events.getJSONObject(i);
            int rodId = object.getInt("id");
            //считаем длительность
            String time = object.getString("timer");
            time = time.substring(time.indexOf(":") + 1);
            String[] timeValues = time.split(":");
            int startTime = Integer.parseInt(timeValues[0]) * 60 + Integer.parseInt(timeValues[1]);
            //считаем время начала
            String eventTime = object.getString("castDate");
            eventTime = eventTime.substring(eventTime.indexOf("T") + 1);
            String[] eventValues = eventTime.split(":");
            int eventEndsAt = Integer.parseInt(eventValues[0]) * 360 + Integer.parseInt(eventValues[1]) * 60 + Integer.parseInt(eventValues[2]) + startTime;
            int timeLeft = eventEndsAt - getCurrentTime();
            String event =  object.getString("event");
            if (timeLeft > 0 && (event.equals("1") || event.equals("2"))) {
                timers[rodId - 1] = new CountDownTimer(timeLeft * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        createNotification(context, rodId);
                    }
                };
                timers[rodId - 1].start();
            }
        }

    }

    public void stopTimer(int rodId) {
        if (timers[rodId - 1] != null) {
            timers[rodId - 1].cancel();
            timers[rodId - 1] = null;
        }
    }

    public static void createNotification(Context context, int rodId) {

        Uri ringURI =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "g_carpfishing";
            String Description = "G-Carpfishing Notify";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.rod_icon)
                        .setContentTitle("G-CARPFISHING")
                        .setContentText("Вышло время заброса удочки номер " + rodId + "!")
                        .setSound(ringURI);

        notificationManager.notify(rodId, builder.build());

    }

    public static int getCurrentTime() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String[] timeSplit = df.format(Calendar.getInstance().getTime()).split(":");
        return Integer.parseInt(timeSplit[0]) * 360 + Integer.parseInt(timeSplit[1]) * 60 + Integer.parseInt(timeSplit[2]);
    }

}
