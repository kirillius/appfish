package com.linaverde.fishingapp.services;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;

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

    public static void createInputDialog(Context context, LayoutInflater inflater, String text, final CompleteActionListener listener) {
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
}
