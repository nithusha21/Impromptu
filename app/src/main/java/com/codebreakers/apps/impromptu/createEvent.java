package com.codebreakers.apps.impromptu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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


public class createEvent extends Fragment {

    LinearLayout Struct;
    Button proceed, invite;

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

        invite = (Button) v.findViewById(R.id.invite);
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
    public void FriendsListCheckBox(){
        get();
        String[] Friends= {"ajay","akshay","mohammed","Rajat","Mohammed"};
        CheckBox[] FriendsInvite = new CheckBox[Friends.length];

        for(int i=0;i<Friends.length;i++){
            FriendsInvite[i]=new CheckBox(getActivity());
            FriendsInvite[i].setText(Friends[i]);
            FriendsInvite[i].setLayoutParams(new FrameLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
            Struct.addView(FriendsInvite[i]);
        }
    }

    public void get() {
        String urlString = "https://api.backendless.com/v1/data/Users";
        try {
            URL url = new URL(urlString);

            createEvent.getJSON task = new createEvent.getJSON();
            task.execute(new URL(urlString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class getJSON extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls){


            try {
                HttpURLConnection urlConnection = (HttpURLConnection) urls[0].openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.setConnectTimeout(30000);
                urlConnection.setReadTimeout(30000);

                urlConnection.addRequestProperty("application-Id", "48449D26-77AF-D84C-FFBC-96A6E338F700");
                urlConnection.addRequestProperty("secret-key","823D3BBA-94A9-3405-FFCE-D3F4BD1C1100");
                urlConnection.addRequestProperty("Content-type", "application/json");
                urlConnection.connect();
                if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.i("HTTP", "Failed");
                }
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                char[] buffer = new char[1024];

                String jsonString;

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();

                jsonString = sb.toString();

                Log.i("JSON: ",jsonString);
                return new JSONObject(jsonString);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            Log.i("Output",jsonObject.toString());

        }
    }



}
