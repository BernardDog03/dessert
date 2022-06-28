package com.example.dessertin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import gr.net.maroulis.library.EasySplashScreen;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(splash.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(3000)
                .withBackgroundColor(Color.parseColor("#000000"))
                .withLogo(R.drawable.logo)
                .withFooterText("Hmmmmm");
        config.getFooterTextView().setTextColor(Color.WHITE);


        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
    }
}
