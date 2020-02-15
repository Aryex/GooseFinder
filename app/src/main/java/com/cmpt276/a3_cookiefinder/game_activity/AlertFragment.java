package com.cmpt276.a3_cookiefinder.game_activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.cmpt276.a3_cookiefinder.MainMenuActivity;
import com.cmpt276.a3_cookiefinder.R;

public class AlertFragment extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //create the fragment View
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.msg_game_over, null);


        //create a button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        };

        //build alert dialog in our view
        return new AlertDialog.Builder(getActivity())
                .setTitle("Game Over!")
                .setView(view)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }
}
