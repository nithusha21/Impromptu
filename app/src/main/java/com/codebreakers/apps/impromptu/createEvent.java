package com.codebreakers.apps.impromptu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.codebreakers.apps.impromptu.R;




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
        String[] Friends= {"ajay","akshay","mohammed","Rajat","Mohammed"};
        CheckBox[] FriendsInvite = new CheckBox[Friends.length];

        for(int i=0;i<Friends.length;i++){
            FriendsInvite[i]=new CheckBox(getActivity());
            FriendsInvite[i].setText(Friends[i]);
            FriendsInvite[i].setLayoutParams(new FrameLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
            Struct.addView(FriendsInvite[i]);
        }
    }

}
