package com.cmpt276.a3_cookiefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmpt276.a3_cookiefinder.game_activity.GameActivity;

public class MainMenuActivity extends AppCompatActivity {

    public static Intent getLaunchIntent(Context context){
        return new Intent(context, MainMenuActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setupPlayButton();
        setupOptionButton();
        setupAboutButton();
    }

    private void setupAboutButton() {
        Button btnPlay = findViewById(R.id.btnAbout);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AboutActivity.getLaunchIntent(MainMenuActivity.this));
            }
        });
    }

    private void setupOptionButton() {
        Button btnPlay = findViewById(R.id.btnOptions);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(OptionsActivity.makeLaunchIntent(MainMenuActivity.this));
            }
        });
    }

    private void setupPlayButton() {
        Button btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GameActivity.getLaunchIntent(MainMenuActivity.this));
            }
        });
    }

}
