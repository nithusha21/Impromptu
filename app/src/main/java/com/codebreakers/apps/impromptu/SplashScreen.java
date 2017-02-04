package com.codebreakers.apps.impromptu;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.codebreakers.apps.impromptu.R;

/**
 * Created by Mohammed Khandwawala on 04-02-2017.
 */

public class SplashScreen extends Activity {

    ProgressBar mprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        Animation anim1 = AnimationUtils.loadAnimation(this,R.anim.anim_down);
        ImageView img =(ImageView)findViewById(R.id.imageView);
        img.setAnimation(anim1);

        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        ObjectAnimator anim = ObjectAnimator.ofInt(mprogressBar, "progress", 0, 100);
        anim.setDuration(2000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this,MainActivity.class));
                finish();

            }
        },3000);
    }
}