package com.codebreakers.apps.impromptu;

import android.os.Bundle;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.codebreakers.apps.impromptu.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.country;


public class createEvent extends Fragment {

    LinearLayout Struct;
    Button proceed, invite;
    String jsonString;

    LocationManager locationManager;
    LocationListener locationListener;

    public createEvent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_event, container, false);
        Struct= (LinearLayout) v.findViewById(R.id.FriendsList);
        proceed = (Button) v.findViewById(R.id.Proceed);

        FriendsListCheckBox();
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Could Be A point of Error
                Intent next = new Intent( getActivity(), PreferenceForm.class);
                startActivity(next);
            }
        });

        invite = (Button) v.findViewById(R.id.invitenew);
        invite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DialogFragment f =  new inviteDialogFragment();
                f.show(getActivity().getSupportFragmentManager(), "invite");
            }
        });


        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //Function to declare checkbox of friends
    public void FriendsListCheckBox() {
        try {
            JSONArray data = MainControlActivity.getJsonFriends().getJSONArray("data");
            Log.i("DATA", data.toString());
            JSONArray friends = new JSONArray();
            for (int i = 0; i < data.length(); i++) {
                if (data.getJSONObject(i).getString("email") == MainControlActivity.getUser()) {
                    String s = data.getJSONObject(i).getString("friends");
                    friends = new JSONArray(s);
                    Log.i("Friends", friends.toString());
                }
            }
            String[] Friends = {"ajay", "akshay", "mohammed", "Rajat", "Mohammed"};
            CheckBox[] FriendsInvite = new CheckBox[Friends.length];

            for (int i = 0; i < friends.length(); i++) {
                FriendsInvite[i] = new CheckBox(getActivity());
                FriendsInvite[i].setText(friends.getString(i));
                FriendsInvite[i].setLayoutParams(new FrameLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                Struct.addView(FriendsInvite[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
