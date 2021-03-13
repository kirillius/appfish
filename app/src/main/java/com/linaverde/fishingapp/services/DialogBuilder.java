package com.linaverde.fishingapp.services;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.models.FishDictionaryItem;
import com.linaverde.fishingapp.models.Violation;
import com.linaverde.fishingapp.models.ViolationDictionaryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DialogBuilder {

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

    public static void createInputNumberDialog(Context context, LayoutInflater inflater, String text, final CompleteActionListener listener) {
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

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOk(etNumber.getText().toString());
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
        }

    }

    public static void createRodSettingsSelectDialog(Context context, LayoutInflater inflater, String text, JSONArray dict, String selectedId, final CompleteActionListener listener) {
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

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOk(adapter.getItem(adapter.getSelected()).toString());
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
}
