package com.codebreakers.apps.impromptu;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codebreakers.apps.impromptu.R;

public class RegisterForm extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            super.onCreate(savedInstanceState);
        }
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            setContentView(R.layout.activity_register_form);
        }
    }
}
