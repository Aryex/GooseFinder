package com.cmpt276.a3_cookiefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ImageView cookieImgView = findViewById(R.id.imageViewCookieIntro);
        View rootLayout = findViewById(R.id.constraintLayoutIntro);
        float layoutHeight = rootLayout.getHeight();

        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(cookieImgView, "y", 1000, 0).setDuration(3000);
        heightAnimator.start();

    }

}
