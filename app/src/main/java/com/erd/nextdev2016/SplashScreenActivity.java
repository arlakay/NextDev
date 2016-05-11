package com.erd.nextdev2016;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by ILM on 5/6/2016.
 */
public class SplashScreenActivity extends BaseActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, ScrollableTabsActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
