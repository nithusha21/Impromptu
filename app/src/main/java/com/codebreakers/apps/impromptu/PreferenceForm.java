package com.codebreakers.apps.impromptu;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class PreferenceForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_form);
        TextView tx = (TextView)findViewById(R.id.cuisine);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/abc.ttf");
        tx.setTypeface(custom_font);

        CuisListCheckBox();

    }
    public void CuisListCheckBox(){
        String[] CuisineList= {"Chinese","Asian"};
        CheckBox[] CuiList=new CheckBox[CuisineList.length];
        LinearLayout Cuisine= (LinearLayout)findViewById(R.id.CuisList);

            for(int i=0;i<CuisineList.length;i++){
                CuiList[i]=new CheckBox(this);
                CuiList[i].setText(CuisineList[i]);
                CuiList[i].setLayoutParams(new FrameLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                Cuisine.addView(CuiList[i]);
            }
        final TextView value = new TextView(this);
        value.setText("0");
        SeekBar price = (SeekBar)findViewById(R.id.PriceSlider);
        price.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value.setText(""+(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Cuisine.addView(value);
    }
}
