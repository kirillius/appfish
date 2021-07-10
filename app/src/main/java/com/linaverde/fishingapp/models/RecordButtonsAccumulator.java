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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RecordButtonsAccumulator {

    private Context context;
    private int rodId;
    private RelativeLayout setBtn, biteBtn, fishBtn, goneBtn;
    private TextView tvTimer;
    private ImageView setBack, biteBack, fishBack, goneBack;
    private int startTime;
    private CountDownTimer timer;
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
    TimerPauseValue pausedValues;
    String timeStr;
    TimersCollection collection;

    public RecordButtonsAccumulator(Context context, String teamId, TextView tvTimer, JSONObject info,
                                    TextView spod, TextView cobr,
                                    ContentLoadingProgressBar progressBar) {
        this.context = context;
        this.tvTimer = tvTimer;
        this.teamId = teamId;
        this.progressBar = progressBar;
        this.spodCounter = spod;
        this.cobrCounter = cobr;

        pausedValues = new TimerPauseValue(context);

        CastTimerAccumulator timerAccumulator = CastTimerAccumulator.getInstance();
        collection = TimersCollection.getInstance();

        try {
            rodId = info.getInt("id");
            event = info.getString("event");
            String time = info.getString("timer");

            time = time.substring(time.indexOf("T") + 1);
            timeStr = time;
            Log.d("Timer", timeStr);
            this.tvTimer.setText(timeStr);
            String[] timeValues = time.split(":");
            this.startTime = Integer.parseInt(timeValues[0]) * 60 * 60 + Integer.parseInt(timeValues[1]) * 60 + Integer.parseInt(timeValues[2]);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            //установка таймера в кастомное значение, если уже был заброс
            if (event.equals("1")) {
                Date dateCastEndsAt = format.parse(info.getString("castDate"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateCastEndsAt);
                calendar.add(Calendar.SECOND, startTime);
                dateCastEndsAt = calendar.getTime();
                Date currDate = Calendar.getInstance().getTime();
                long diff = dateCastEndsAt.getTime() - currDate.getTime();
                if (diff > 0 && diff < startTime * 1000) {
                    timerAccumulator.stopTimer(rodId);
                    timer = new CountDownTimer(diff, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long millis = millisUntilFinished;
                            //Convert milliseconds into hour,minute and seconds
                            String hms = String.format("%02d:%02d:%02d",
                                    TimeUnit.MILLISECONDS.toHours(millis),
                                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                            tvTimer.setText(hms);//set text
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
                    collection.addTimer(timer);
                    timer.start();
                } else {
                    timeIsAlreadyOut = true;
                    tvTimer.setText("00:00:00");
                }
            }
            else if (event.equals("5")) {
                tvTimer.setText(timeStr);
            }

            spodCounter.setText(info.getString("spod"));
            cobrCounter.setText(info.getString("cobra"));

            spodStart = info.getInt("spod");
            cobrStart = info.getInt("cobra");

            setCounters();

        } catch (JSONException | ParseException e) {
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
                            event = array.getJSONObject(i).getString("event");
                            setBack(event);
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
                if (event.equals("5") || event.equals("3") || event.equals("4")) {
                    if (timer != null)
                        timer.cancel();
                    timer = new CountDownTimer(startTime * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long millis = millisUntilFinished;
                            //Convert milliseconds into hour,minute and seconds
                            String hms = String.format("%02d:%02d:%02d",
                                    TimeUnit.MILLISECONDS.toHours(millis),
                                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                            tvTimer.setText(hms);//set text
                        }

                        @Override
                        public void onFinish() {
                            tvTimer.setText("00:00:00");
                            CastTimerAccumulator.createNotification(context, rodId);
                            setBackToBlack();
                            setBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_red));
                            Animation animation = AnimationUtils.loadAnimation(context, R.anim.simple_alpha);
                            setBack.startAnimation(animation);
                        }
                    };
                    collection.addTimer(timer);
                    timer.start();
                    collection.clearNull();

                    progressBar.show();
                    requestHelper.executePost("catching", new String[]{"team", "rod", "event", "time"},
                            new String[]{teamId, Integer.toString(rodId), "1", getCurrentDateTime()}, null, listener);
                } else if (event.equals("1")) {
                    //pausedValues.saveValue(Integer.toString(rodId), tvTimer.getText().toString());
                    if (timer != null)
                        timer.cancel();
                    collection.clearNull();
                    tvTimer.setText(timeStr);
                    //tvTimer.setText(pausedValues.getPausedTime(Integer.toString(rodId)));
                    requestHelper.executePost("catching", new String[]{"team", "rod", "event", "time"},
                            new String[]{teamId, Integer.toString(rodId), "5", getCurrentDateTime()}, null, listener);
                }


            }
        });

        biteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Event", "bite" + rodId + "clicked");

                if (event.equals("1")) {
                    if (timer != null)
                        timer.cancel();
                    progressBar.show();
                    requestHelper.executePost("catching", new String[]{"team", "rod", "event", "time"},
                            new String[]{teamId, Integer.toString(rodId), "2", getCurrentDateTime()}, null, listener);
                }
            }
        });


        fishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Event", "fish" + rodId + "clicked");
                if (event.equals("2")) {
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
                if (event.equals("2")) {
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
                    break;
                case "3":
                    fishBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_green));
                    break;
                case "4":
                    goneBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_red));
                    break;
                case "5":
                    setBack.setImageDrawable(context.getDrawable(R.drawable.record_btn_red));
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
