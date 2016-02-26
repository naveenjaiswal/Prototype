package com.li8tech.nli8.prototype;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ImageView gyroView = (ImageView) findViewById(R.id.imageViewSplash);



        gyroView.setBackgroundResource(R.drawable.gyro_animation);



        AnimationDrawable gyroAnimation = (AnimationDrawable) gyroView.getBackground();

        gyroAnimation.start();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over

               // SharedPreferences shf = getSharedPreferences("LogggedInUserName", Context.MODE_PRIVATE);
             //   String strPref = shf.getString("UserName", null);

              //  if(strPref != null) {
                    // do some thing
                    //Redirect to mainactivity
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);

                    // Start your app main activity
              //  }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
