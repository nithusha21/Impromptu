package com.codebreakers.apps.impromptu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RankistActivity extends AppCompatActivity {

    TextView ranklist;
    Button close;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranklist_dialog);
        ranklist = (TextView) findViewById(R.id.ranklistTextView);
        close = (Button) findViewById(R.id.closedialog);

        for (int i = 0; i < MainActivity.tester.names.length; i++) {
            ranklist.setText(ranklist.getText() + "\n" + (i + 1) + " " + MainActivity.tester.answers[i]);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(RankistActivity.this, MapsActivity.class);
                startActivity(next);

            }
        });

    }
}
