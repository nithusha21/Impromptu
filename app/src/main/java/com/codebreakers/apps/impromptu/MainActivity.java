package com.codebreakers.apps.impromptu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codebreakers.apps.impromptu.R;

public class MainActivity extends AppCompatActivity {

    RestaurantFinder tester = new RestaurantFinder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tester.findRestaurants(12.9845,80.2330);
    }
}
