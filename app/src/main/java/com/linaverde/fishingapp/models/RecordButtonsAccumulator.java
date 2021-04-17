package com.linaverde.fishingapp.models;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.activities.AuthActivity;
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
    TextView spodCounter, cobrCounter;
    ContentLoadingProgressBar progressBar;
    boolean timeIsAlreadyOut = false;
    CountDownTimer counterClicked;
    int spodStart, cobrStart;
    int tempSpod, tempCobr;

    public RecordButtonsAccumulator(Context context, String teamId, TextView tvTimer, JSONObject info,
                                    TextView spod, TextView cobr,
                                    ContentLoadingProgressBar progressBar) {
        this.context = context;
        this.tvTimer = tvTimer;
        this.teamId = teamId;
        this.progressBar = progressBar;
        this.spodCounter = spod;
        this.cobrCounter = cobr;

        CastTimerAccumulator timerAccumulator = CastTimerAccumulator.getInstance();

        try {
            rodId = info.getInt("id");
            event = info.getString("event");
            String time = info.getString("timer");
            time = time.substring(time.indexOf(":") + 1);
            this.tvTimer.setText(time);
            String[] timeValues = time.split(":");
            this.startTime = Integer.parseInt(timeValues[0]) * 60 + Integer.parseInt(timeValues[1]);

            //установка таймера в кастомное значение, если уже был заброс
            if (event.equals("1")) {
                String eventTime = info.getString("castDate");
                eventTime = eventTime.substring(eventTime.indexOf("T") + 1);
                String[] eventValues = eventTime.split(":");
                int eventEndsAt = Integer.parseInt(eventValues[0]) * 360 + Integer.parseInt(eventValues[1]) * 60 + Integer.parseInt(eventValues[2]) + this.startTime;
                Log.d(Integer.toString(rodId) + "event ends at", Integer.toString(eventEndsAt));
                int timeLeft = eventEndsAt - CastTimerAccumulator.getCurrentTime();
                Log.d(Integer.toString(rodId) + " currTime", Integer.toString(timeLeft));
                Log.d(Integer.toString(rodId) + " time left", Integer.toString(timeLeft));
                if (timeLeft > 0 && timeLeft <= startTime && (event.equals("1") || event.equals("2"))) {
                    timerAccumulator.stopTimer(rodId);
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
                            CastTimerAccumulator.createNotification(context, rodId);
                            if (setBack != null) {
                                setBackToBlack();
                                setBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_red));
                                Animation animation = AnimationUtils.loadAnimation(context, R.anim.simple_alpha);
                                setBack.startAnimation(animation);
                            }

                        }
                    };
                    timer.start();
                } else if (timeLeft <= 0 || timeLeft > startTime) {
                    timeIsAlreadyOut = true;
                }
            }

            spodCounter.setText(info.getString("spod"));
            cobrCounter.setText(info.getString("cobra"));

            spodStart = info.getInt("spod");
            cobrStart = info.getInt("cobra");

            setCounters();

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
                        CastTimerAccumulator.createNotification(context, rodId);
                        setBackToBlack();
                        setBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_red));
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.simple_alpha);
                        setBack.startAnimation(animation);
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
                if (timer != null)
                    timer.cancel();
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


    private void setBack(String event) {
        if (!timeIsAlreadyOut) {
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
        } else {
            setBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_red));
            timeIsAlreadyOut = false;
        }
    }

    private void setBackToBlack() {
        setBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_back));
        biteBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_back));
        fishBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_back));
        goneBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_back));
    }

    private void setCounters() {

        RequestListener li = new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                Log.d("Request", "Молодцы огурцы все отправили");
                spodStart = tempSpod;
                cobrStart = tempCobr;
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(context, LayoutInflater.from(context), context.getString(R.string.request_error), null);
                spodCounter.setText(Integer.toString(spodStart));
                cobrCounter.setText(Integer.toString(cobrStart));

            }
        };

        counterClicked = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                try {
                    progressBar.show();
                    JSONArray json = new JSONArray();
                    JSONObject spodParam = new JSONObject();
                    spodParam.put("paramId", "SPOD_QTY");
                    spodParam.put("valueId", Integer.parseInt(spodCounter.getText().toString()));
                    JSONObject cobrParam = new JSONObject();
                    cobrParam.put("paramId", "COBR_QTY");
                    cobrParam.put("valueId", Integer.parseInt(cobrCounter.getText().toString()));
                    json.put(spodParam);
                    json.put(cobrParam);
                    requestHelper.executePost("rods", new String[]{"team", "rod", "rodType"},
                            new String[]{teamId, Integer.toString(rodId), "carp"},
                            json.toString(), li);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.hide();
                }
            }
        };

        spodCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterClicked.cancel();
                int curr = Integer.parseInt(spodCounter.getText().toString());
                curr += 1;
                spodCounter.setText(Integer.toString(curr));
                tempSpod = curr;
                counterClicked.start();
            }
        });

        spodCounter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                counterClicked.cancel();
                int curr = Integer.parseInt(spodCounter.getText().toString());
                if (curr > 0)
                    curr -= 1;
                tempSpod = curr;
                spodCounter.setText(Integer.toString(curr));
                counterClicked.start();
                return true;
            }
        });

        cobrCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterClicked.cancel();
                int curr = Integer.parseInt(cobrCounter.getText().toString());
                curr += 1;
                tempCobr = curr;
                cobrCounter.setText(Integer.toString(curr));
                counterClicked.start();
            }
        });

        cobrCounter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                counterClicked.cancel();
                int curr = Integer.parseInt(cobrCounter.getText().toString());
                if (curr > 0)
                    curr -= 1;
                tempSpod = curr;
                cobrCounter.setText(Integer.toString(curr));
                counterClicked.start();
                return true;
            }
        });

    }

}
