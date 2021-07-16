package com.linaverde.fishingapp.services;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.RodsSettingsParamSwithcListener;
import com.linaverde.fishingapp.models.FishDictionaryItem;
import com.linaverde.fishingapp.models.ViolationDictionaryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class DialogBuilder {

    private static final int MAX_HEIGHT = 1200;

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void createDefaultDialog(Context context, LayoutInflater inflater, String text, final CompleteActionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewDialog = inflater.inflate(R.layout.dialog_default, null);
        TextView tvText, tvOk;
        tvText = viewDialog.findViewById(R.id.tv_dialog);
        tvOk = viewDialog.findViewById(R.id.tv_ok);

        if (text != null)
            tvText.setText(text);

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onOk(null);
                dialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }

    }

    public static void createTwoButtons(Context context, LayoutInflater inflater, String text, final CompleteActionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewDialog = inflater.inflate(R.layout.dialog_two_buttons, null);
        TextView tvText, tvOk, tvCancel;
        tvText = viewDialog.findViewById(R.id.tv_dialog);
        tvOk = viewDialog.findViewById(R.id.tv_ok);
        tvCancel = viewDialog.findViewById(R.id.tv_cancel);

        if (text != null)
            tvText.setText(text);

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOk(null);
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                dialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }

    }

    public static void createInputNumberDialog(Context context, LayoutInflater inflater, String text, boolean pin, final CompleteActionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewDialog = inflater.inflate(R.layout.dialog_edit_number, null);
        TextView tvText, tvOk, tvCancel;
        EditText etNumber;
        tvText = viewDialog.findViewById(R.id.tv_dialog);
        tvOk = viewDialog.findViewById(R.id.tv_ok);
        tvCancel = viewDialog.findViewById(R.id.tv_cancel);
        etNumber = viewDialog.findViewById(R.id.et_dialog);

        if (text != null)
            tvText.setText(text);

        if (pin) {
            etNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            etNumber.setHint("ПИН");
            etNumber.setHintTextColor(context.getResources().getColor(R.color.gray, null));
        } else {
            etNumber.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        }

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOk(etNumber.getText().toString());
                hideKeyboardFrom(context, viewDialog);
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                hideKeyboardFrom(context, viewDialog);
                dialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }

    }



    public static void createInputStringDialog(Context context, LayoutInflater inflater, String text, final CompleteActionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewDialog = inflater.inflate(R.layout.dialog_edit_string, null);
        TextView tvText, tvOk, tvCancel;
        EditText etString;
        tvText = viewDialog.findViewById(R.id.tv_dialog);
        tvOk = viewDialog.findViewById(R.id.tv_ok);
        tvCancel = viewDialog.findViewById(R.id.tv_cancel);
        etString = viewDialog.findViewById(R.id.et_dialog);

        if (text != null)
            tvText.setText(text);

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOk(etString.getText().toString());
                hideKeyboardFrom(context, viewDialog);
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                hideKeyboardFrom(context, viewDialog);
                dialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }

    }


    public static void createSelectFishTypeDialog(Context context, LayoutInflater inflater, String text, FishDictionaryItem[] dict, String selectedId, final CompleteActionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewDialog = inflater.inflate(R.layout.dialog_listview, null);
        TextView tvText, tvOk, tvCancel;
        ListView listView;
        tvText = viewDialog.findViewById(R.id.tv_dialog);
        tvOk = viewDialog.findViewById(R.id.tv_ok);
        tvCancel = viewDialog.findViewById(R.id.tv_cancel);

        listView = viewDialog.findViewById(R.id.lv_dialog);

        if (text != null)
            tvText.setText(text);

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        int selected = -1;
        for (int i = 0; i < dict.length; i++) {
            if (dict[i].getId().equals(selectedId)) {
                selected = i;
                break;
            }
        }

        FishTypeAdapter adapter = new FishTypeAdapter(context, dict, selected);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelected(position);
                adapter.notifyDataSetChanged();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOk(adapter.getItem(adapter.getSelected()).getId());
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                dialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing()) {
            dialog.show();

            ViewTreeObserver vto = viewDialog.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    viewDialog.getViewTreeObserver().removeOnPreDrawListener(this);
                    Log.d("Dialog width", Integer.toString(viewDialog.getMeasuredWidth()));
                    Log.d("Dialog height", Integer.toString(viewDialog.getMeasuredHeight()));
                    if (viewDialog.getMeasuredHeight() > MAX_HEIGHT) {
                        //dialog.getWindow().setLayout(viewDialog.getMeasuredWidth(), MAX_HEIGHT);
                        ViewGroup.LayoutParams lp =  viewDialog.getLayoutParams();
                        lp.height = MAX_HEIGHT;
                        lp.width = viewDialog.getMeasuredWidth();
                        viewDialog.setLayoutParams(lp);
                    }
                    return true;
                }
            });
        }

    }


    public static void createRodSettingsSelectDialog(Context context, LayoutInflater inflater, String settingName, String text,
                                                     JSONArray dict, String selectedId, final RodsSettingsParamSwithcListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewDialog = inflater.inflate(R.layout.dialog_listview, null);
        TextView tvText, tvOk, tvCancel, tvSetting, tvClear;
        ListView listView;
        tvText = viewDialog.findViewById(R.id.tv_dialog);
        tvOk = viewDialog.findViewById(R.id.tv_ok);
        tvCancel = viewDialog.findViewById(R.id.tv_cancel);
        tvSetting = viewDialog.findViewById(R.id.tv_setting_name);
        tvClear = viewDialog.findViewById(R.id.tv_clear);
        listView = viewDialog.findViewById(R.id.lv_dialog);

        if (text != null)
            tvText.setText(text);

        if (settingName != null){
            tvSetting.setVisibility(View.VISIBLE);
            tvSetting.setText(settingName);

        }

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        JSONObject[] array = new JSONObject[dict.length()];

        int selected = -1;
        try {
            for (int i = 0; i < dict.length(); i++) {
                if (dict.getJSONObject(i).getString("id").equals(selectedId)) {
                    selected = i;
                    break;
                }
            }

            for (int i = 0; i < dict.length(); i++) {
                array[i] = dict.getJSONObject(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RodsObjectSettingsAdapter adapter = new RodsObjectSettingsAdapter(context, array, selected);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelected(position);
                adapter.notifyDataSetChanged();
            }
        });

        EditText addinfo;
        addinfo = viewDialog.findViewById(R.id.et_addinfo);
        addinfo.setVisibility(View.VISIBLE);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = addinfo.getText().toString();
                if (info.isEmpty()) {
                    listener.onOk(adapter.getItem(adapter.getSelected()).toString(), false);
                } else {
                    listener.onOk(info, true);
                }
                dialog.dismiss();
            }
        });

//        tvClear.setVisibility(View.VISIBLE);
//        tvClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onClear();
//                dialog.dismiss();
//            }
//        });



        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                dialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing()) {
            dialog.show();

            ViewTreeObserver vto = viewDialog.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    viewDialog.getViewTreeObserver().removeOnPreDrawListener(this);
                    Log.d("Dialog width", Integer.toString(viewDialog.getMeasuredWidth()));
                    Log.d("Dialog height", Integer.toString(viewDialog.getMeasuredHeight()));
                    if (viewDialog.getMeasuredHeight() > MAX_HEIGHT) {
                        //dialog.getWindow().setLayout(viewDialog.getMeasuredWidth(), MAX_HEIGHT);
                        ViewGroup.LayoutParams lp =  viewDialog.getLayoutParams();
                        lp.height = MAX_HEIGHT;
                        lp.width = viewDialog.getMeasuredWidth();
                        viewDialog.setLayoutParams(lp);
                    }
                    return true;
                }
            });
        }


    }

    public static void createSelectViolationTypeDialog(Context context, LayoutInflater inflater, String text, ViolationDictionaryItem[] dict, String selectedId, final CompleteActionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewDialog = inflater.inflate(R.layout.dialog_listview, null);
        TextView tvText, tvOk, tvCancel;
        ListView listView;
        tvText = viewDialog.findViewById(R.id.tv_dialog);
        tvOk = viewDialog.findViewById(R.id.tv_ok);
        tvCancel = viewDialog.findViewById(R.id.tv_cancel);

        listView = viewDialog.findViewById(R.id.lv_dialog);

        if (text != null)
            tvText.setText(text);

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        int selected = -1;
        for (int i = 0; i < dict.length; i++) {
            if (dict[i].getId().equals(selectedId)) {
                selected = i;
                break;
            }
        }

        ViolationTypeAdapter adapter = new ViolationTypeAdapter(context, dict, selected);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelected(position);
                adapter.notifyDataSetChanged();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOk(adapter.getItem(adapter.getSelected()).getId());
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                dialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing()) {
            dialog.show();

            ViewTreeObserver vto = viewDialog.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    viewDialog.getViewTreeObserver().removeOnPreDrawListener(this);
                    Log.d("Dialog width", Integer.toString(viewDialog.getMeasuredWidth()));
                    Log.d("Dialog height", Integer.toString(viewDialog.getMeasuredHeight()));
                    if (viewDialog.getMeasuredHeight() > MAX_HEIGHT) {
                        //dialog.getWindow().setLayout(viewDialog.getMeasuredWidth(), MAX_HEIGHT);
                        ViewGroup.LayoutParams lp =  viewDialog.getLayoutParams();
                        lp.height = MAX_HEIGHT;
                        lp.width = viewDialog.getMeasuredWidth();
                        viewDialog.setLayoutParams(lp);
                    }
                    return true;
                }
            });
        }

    }

    public static void createTimeInputDialog(Context context, LayoutInflater inflater, String text, String hint, final CompleteActionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewDialog = inflater.inflate(R.layout.dialog_time, null);
        TextView tvText, tvOk, tvCancel;
        EditText etHours, etMin, etSec;
        tvText = viewDialog.findViewById(R.id.tv_dialog);
        tvOk = viewDialog.findViewById(R.id.tv_ok);
        tvCancel = viewDialog.findViewById(R.id.tv_cancel);

        etHours = viewDialog.findViewById(R.id.et_hours);
        etMin = viewDialog.findViewById(R.id.et_min);
        etSec = viewDialog.findViewById(R.id.et_sec);

        if (hint != null) {
            String[] time = hint.split(":");

            etHours.setText(time[0]);
            etMin.setText(time[1]);
            etSec.setText(time[2]);
        }

        final TextWatcher hoursWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(""))
                    if (Integer.parseInt(s.toString()) > 24) {
                        s.delete(s.length() - 1, s.length());
                    }
            }
        };

        final TextWatcher minsSecWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(""))
                    if (Integer.parseInt(s.toString()) > 59) {
                        s.delete(s.length() - 1, s.length());
                    }
            }
        };

        etHours.addTextChangedListener(hoursWatcher);
        etMin.addTextChangedListener(minsSecWatcher);
        etSec.addTextChangedListener(minsSecWatcher);

        if (text != null)
            tvText.setText(text);

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hours, mins, sec;
                hours = etHours.getText().toString();
                mins = etMin.getText().toString();
                sec = etSec.getText().toString();
                while (hours.length() < 2) {
                    hours = "0" + hours;
                }
                while (mins.length() < 2) {
                    mins = "0" + mins;
                }
                while (sec.length() < 2) {
                    sec = "0" + sec;
                }
                String time = hours + ":" + mins + ":" + sec;
                listener.onOk(time);
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                dialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

    public static void createSelectRegisterStatusDialog(Context context, LayoutInflater inflater, String text, int selected, final CompleteActionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewDialog = inflater.inflate(R.layout.dialog_listview, null);
        TextView tvText, tvOk, tvCancel;
        ListView listView;
        tvText = viewDialog.findViewById(R.id.tv_dialog);
        tvOk = viewDialog.findViewById(R.id.tv_ok);
        tvCancel = viewDialog.findViewById(R.id.tv_cancel);

        listView = viewDialog.findViewById(R.id.lv_dialog);

        if (text != null)
            tvText.setText(text);

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final String[] statuses = context.getResources().getStringArray(
                R.array.register_status);

        StringChooseAdapter adapter = new StringChooseAdapter(context, statuses, selected);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelected(position);
                adapter.notifyDataSetChanged();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = adapter.getSelected();
                if (selected == 0) {
                    listener.onOk(Integer.toString(1));
                } else if (selected == 1) {
                    listener.onOk(Integer.toString(0));
                } else {
                    listener.onOk(Integer.toString(selected));
                }
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                dialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing()) {
            dialog.show();

            ViewTreeObserver vto = viewDialog.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    viewDialog.getViewTreeObserver().removeOnPreDrawListener(this);
                    Log.d("Dialog width", Integer.toString(viewDialog.getMeasuredWidth()));
                    Log.d("Dialog height", Integer.toString(viewDialog.getMeasuredHeight()));
                    if (viewDialog.getMeasuredHeight() > MAX_HEIGHT) {
                        //dialog.getWindow().setLayout(viewDialog.getMeasuredWidth(), MAX_HEIGHT);
                        ViewGroup.LayoutParams lp =  viewDialog.getLayoutParams();
                        lp.height = MAX_HEIGHT;
                        lp.width = viewDialog.getMeasuredWidth();
                        viewDialog.setLayoutParams(lp);
                    }
                    return true;
                }
            });
        }

    }

    public static void createSelectRodOnMapDialog(Context context, LayoutInflater inflater, String text, int selected, final CompleteActionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewDialog = inflater.inflate(R.layout.dialog_listview, null);
        TextView tvText, tvOk, tvCancel;
        ListView listView;
        tvText = viewDialog.findViewById(R.id.tv_dialog);
        tvOk = viewDialog.findViewById(R.id.tv_ok);
        tvCancel = viewDialog.findViewById(R.id.tv_cancel);

        listView = viewDialog.findViewById(R.id.lv_dialog);

        if (text != null)
            tvText.setText(text);

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final String[] statuses = context.getResources().getStringArray(
                R.array.rods_nums);

        StringChooseAdapter adapter = new StringChooseAdapter(context, statuses, selected);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelected(position);
                adapter.notifyDataSetChanged();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getSelected() >= 0)
                    listener.onOk(Integer.toString(adapter.getSelected() + 1));
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                dialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }

    }
}
