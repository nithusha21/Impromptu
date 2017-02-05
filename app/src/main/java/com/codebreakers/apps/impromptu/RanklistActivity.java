package com.codebreakers.apps.impromptu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Rajat on 05-02-2017.
 */

public class RanklistActivity extends Activity {


    TextView ranklist;
    Button close;
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ranklist_dialog);
        ranklist = (TextView) findViewById(R.id.ranklistTextView);
        close = (Button) findViewById(R.id.closedialog);

        for(int i=0;i<MainActivity.tester.names.length;i++){
            ranklist.setText(ranklist.getText()+"\n"+(i+1)+" "+MainActivity.tester.names[i]);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next=new Intent(RanklistActivity.this,MapsActivity.class);

            }
        });

    }
}
