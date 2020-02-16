package com.cmpt276.a3_cookiefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        animateCogwheel();
        Button btnSkip = findViewById(R.id.btnIntroActSkip);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endIntro();
            }
        });

    }

    private void endIntro() {
        startActivity(MainMenuActivity.getLaunchIntent(IntroActivity.this));
        finish();
    }

    private void animateCogwheel() {
        ImageView cogwheel = findViewById(R.id.imageViewCogWheel);
        Animation animation = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.rotate);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                endIntro();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        cogwheel.startAnimation(animation);

        ImageView cogwheelLeft = findViewById(R.id.imageViewCogWheel2);
        Animation animation2 = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.rotate2);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                endIntro();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        cogwheelLeft.startAnimation(animation2);

    }

}
