package com.linaverde.fishingapp.models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.linaverde.fishingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.client.utils.DateUtils;

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

    public void createTimers(Context context, JSONArray events) throws JSONException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        timersAlreadyCreated = true;
        for (int i = 0; i < events.length(); i++) {
            JSONObject object = events.getJSONObject(i);
            int rodId = object.getInt("id");
            String event = object.getString("event");
            if (event.equals("1")) {
                String time = object.getString("timer");
                time = time.substring(time.indexOf(":") + 1);
                String[] timeValues = time.split(":");
                int startTime = Integer.parseInt(timeValues[0]) * 60 + Integer.parseInt(timeValues[1]);
                Date dateCastEndsAt = format.parse(object.getString("castDate"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateCastEndsAt);
                calendar.add(Calendar.SECOND, startTime);
                dateCastEndsAt = calendar.getTime();
                Date currDate = Calendar.getInstance().getTime();
                long diff = dateCastEndsAt.getTime() - currDate.getTime();
                if (diff > 0 && diff < startTime * 1000) {
                    timers[rodId - 1] = new CountDownTimer(diff, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            createNotification(context, rodId);
                        }
                    };
                    timers[rodId - 1].start();
                    Log.d("Timer acc", rodId + " timer set");
                }
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
                Uri.parse("android.resource://"
                        + context.getPackageName() + "/"
                        + R.raw.alarm);
        if (ringURI == null) {
            Log.d("ringUri", "is null");
            ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        context.grantUriPermission("com.linaverde.fishingapp", ringURI, Intent.FLAG_GRANT_READ_URI_PERMISSION);

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

            if (ringURI != null) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();
                mChannel.setSound(ringURI, audioAttributes);
            }

            notificationManager.createNotificationChannel(mChannel);
        }


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.rod_icon)
                        .setContentTitle("G-CARPFISHING")
                        .setContentText("Вышло время заброса удочки номер " + rodId + "!")
                        .setSound(ringURI)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

        notificationManager.notify(rodId, builder.build());

    }

    public static int getCurrentTime() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String[] timeSplit = df.format(Calendar.getInstance().getTime()).split(":");
        return Integer.parseInt(timeSplit[0]) * 360 + Integer.parseInt(timeSplit[1]) * 60 + Integer.parseInt(timeSplit[2]);
    }

}
