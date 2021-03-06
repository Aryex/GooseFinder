package com.cmpt276.a3_cookiefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.drm.DrmStore;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        animateCogwheel();
        animateTexts();
        setupSkipButton();
    }

    private void animateTexts() {
        LinearLayout linearLayout = findViewById(R.id.linearLayoutIntro);
       /* ImageView goose = (ImageView) getLayoutInflater().inflate(R.layout.activity_intro, null);
        View view = findViewById(R.id.gooseSign);*/
        ImageView gooseSign = findViewById(R.id.gooseSign);
        int[] gooseLocation = new int[2];
        gooseSign.getLocationOnScreen(gooseLocation);
        //int layoutWidth = linearLayout.getWidth();
        int destination = 750;
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "x",1800, destination).setDuration(6000);
        animator.start();
        //Log.i("INTRO", ""+ destinationX);
    }

    private void setupSkipButton() {
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
        this.finish();
    }

    private void animateCogwheel() {
        /*ImageView cogwheel = findViewById(R.id.imageViewCogWheel);
        Animation animation = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.rotate);
        cogwheel.startAnimation(animation);*/
        TextView loading = findViewById(R.id.textViewLoading);
        ImageView cogwheelLeft = findViewById(R.id.imageViewCogWheel2);
        Animation animation2 = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.rotate2);
        Animation blinking = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.blink_anim);

        cogwheelLeft.startAnimation(animation2);
        loading.startAnimation(blinking);

        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int milisec = 4000;
                setupDelayedEnd(milisec);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void setupDelayedEnd(int milisec) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                endIntro();
            }
        }, milisec);
    }

}
