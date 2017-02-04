package com.codebreakers.apps.impromptu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codebreakers.apps.impromptu.R;

public class RegisterForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_register_form);
    }
}
