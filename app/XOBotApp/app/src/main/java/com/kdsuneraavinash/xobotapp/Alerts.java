package com.kdsuneraavinash.xobotapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.widget.EditText;

class Alerts {
    static void showOptionDialog(Context context, String title, final String[] options, final OptionDialogResultListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle(title)
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onOption(which, options[which]);
                    }
                }).show();
    }

    static void showInputDialogBox(Context context, String title, String defaultText, final TextDialogResultListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        // Set up the input
        final EditText dialogInput = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        dialogInput.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogInput.setHint(defaultText);
        dialogInput.setHintTextColor(Color.GRAY);
        dialogInput.setTextColor(Color.BLACK);

        builder.setTitle(title)
                .setView(dialogInput)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onText(dialogInput.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();

    }
}

interface OptionDialogResultListener {
    void onOption(int option, String optionText);
}

interface TextDialogResultListener {
    void onText(String text);
}