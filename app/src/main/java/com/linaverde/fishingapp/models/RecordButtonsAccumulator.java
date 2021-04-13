package com.linaverde.fishingapp.models;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.ContentLoadingProgressBar;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.RequestHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RecordButtonsAccumulator {

    private Context context;
    private int rodId;
    private RelativeLayout setBtn, biteBtn, fishBtn, goneBtn;
    private TextView tvTimer;
    private ImageView setBack, biteBack, fishBack, goneBack;
    private int startTime;
    private CountDownTimer timer;
    boolean wasBite;
    RequestHelper requestHelper;
    RequestListener listener;
    String teamId;
    String event;
    ContentLoadingProgressBar progressBar;

    public RecordButtonsAccumulator(Context context, String teamId, TextView tvTimer, JSONObject info, ContentLoadingProgressBar progressBar) {
        this.context = context;
        this.tvTimer = tvTimer;
        this.teamId = teamId;
        this.progressBar = progressBar;

        try {
            rodId = info.getInt("id");
            event = info.getString("event");
            String time = info.getString("timer");
            time = time.substring(time.indexOf(":") + 1);
            this.tvTimer.setText(time);
            String[] timeValues = time.split(":");
            this.startTime = Integer.parseInt(timeValues[0]) * 60 + Integer.parseInt(timeValues[1]);

            //установка таймера в кастомное значение, если уже был заброс
            if (event != null && !event.equals("null")) {
                String eventTime = info.getString("eventDate");
                eventTime = eventTime.substring(eventTime.indexOf("T") + 1);
                String[] eventValues = eventTime.split(":");
                int eventEndsAt = Integer.parseInt(eventValues[0]) * 360 + Integer.parseInt(eventValues[1]) * 60 + Integer.parseInt(eventValues[2]) + this.startTime;
                int timeLeft = eventEndsAt - getCurrentTime();
//                Log.d("Rod Id", Integer.toString(rodId));
//                Log.d("Timer settings current time", Integer.toString(getCurrentTime()));
//                Log.d("Timer settings event started", Integer.toString(eventEndsAt));
//                Log.d("Timer settings diff", Integer.toString(timeLeft));
                if (timeLeft > 0) {
                    timer = new CountDownTimer(timeLeft * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            int sec = (int) (millisUntilFinished / 1000);
                            String currTime = Integer.toString((sec) / 60) + ":" + Integer.toString(sec % 60);
                            tvTimer.setText(currTime);
                        }

                        @Override
                        public void onFinish() {
                            tvTimer.setText("00:00");
                        }
                    };
                    timer.start();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        requestHelper = new RequestHelper(context);
        listener = new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                try {
                    JSONArray array = json.getJSONArray("rods");
                    for (int i = 0; i < 4; i++) {
                        if (array.getJSONObject(i).getInt("id") == rodId) {
                            setBack(array.getJSONObject(i).getString("event"));
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(context, LayoutInflater.from(context), context.getString(R.string.request_error), null);
            }
        };
    }

    public void setButtons(RelativeLayout[] btn) {
        this.setBtn = btn[0];
        this.biteBtn = btn[1];
        this.fishBtn = btn[2];
        this.goneBtn = btn[3];
        setOnClick();
    }


    public void setButtonsBack(ImageView[] back) {
        this.setBack = back[0];
        this.biteBack = back[1];
        this.fishBack = back[2];
        this.goneBack = back[3];
        setBack(this.event);
    }

    public int getRodId() {
        return rodId;
    }

    private void setOnClick() {
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Event", "set" + rodId + "clicked");
                String setTime = Integer.toString(startTime / 60) + ":" + Integer.toString(startTime % 60);
                tvTimer.setText(setTime);
                if (timer != null)
                    timer.cancel();
                timer = new CountDownTimer(startTime * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int sec = (int) (millisUntilFinished / 1000);
                        String currTime = Integer.toString((sec) / 60) + ":" + Integer.toString(sec % 60);
                        tvTimer.setText(currTime);
                    }

                    @Override
                    public void onFinish() {
                        tvTimer.setText("00:00");
                    }
                };
                timer.start();

                wasBite = false;
                progressBar.show();
                requestHelper.executePost("catching", new String[]{"team", "rod", "event", "time"},
                        new String[]{teamId, Integer.toString(rodId), "1", getCurrentDateTime()}, null, listener);


            }
        });

        biteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Event", "bite" + rodId + "clicked");
                wasBite = true;
                progressBar.show();
                requestHelper.executePost("catching", new String[]{"team", "rod", "event", "time"},
                        new String[]{teamId, Integer.toString(rodId), "2", getCurrentDateTime()}, null, listener);
            }
        });


        fishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Event", "fish" + rodId + "clicked");
                if (wasBite) {
                    wasBite = false;
                    if (timer != null)
                        timer.cancel();
                    progressBar.show();
                    requestHelper.executePost("catching", new String[]{"team", "rod", "event", "time"},
                            new String[]{teamId, Integer.toString(rodId), "3", getCurrentDateTime()}, null, listener);
                }
            }
        });

        goneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Event", "gone" + rodId + "clicked");
                if (wasBite) {
                    if (timer != null)
                        timer.cancel();
                    wasBite = false;
                    progressBar.show();
                    requestHelper.executePost("catching", new String[]{"team", "rod", "event", "time"},
                            new String[]{teamId, Integer.toString(rodId), "4", getCurrentDateTime()}, null, listener);
                }
            }
        });
    }

    private String getCurrentDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        String[] date = df.format(Calendar.getInstance().getTime()).split(" ");
        String formattedDate = "";
        for (int j = 0; j < date.length; j++) {
            formattedDate += date[j];
            if (j == date.length - 2) {
                formattedDate += "T";
            } else if (j != date.length - 1) {
                formattedDate += "-";
            }
        }
        return formattedDate;
    }

    private int getCurrentTime() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String[] timeSplit = df.format(Calendar.getInstance().getTime()).split(":");
        return Integer.parseInt(timeSplit[0]) * 360 + Integer.parseInt(timeSplit[1]) * 60 + Integer.parseInt(timeSplit[2]);
    }

    private void setBack(String event) {
        setBackToBlack();
        switch (event) {
            case "1":
                setBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_green));
                break;
            case "2":
                biteBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_green));
                wasBite = true;
                break;
            case "3":
                fishBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_green));
                break;
            case "4":
                goneBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_red));
                break;
        }
    }

    private void setBackToBlack() {
        setBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_back));
        biteBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_back));
        fishBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_back));
        goneBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_back));
    }
}
