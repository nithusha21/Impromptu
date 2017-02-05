package com.codebreakers.apps.impromptu;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AddFriend extends Fragment {
    //TextView tx = (TextView)getActivity().findViewById(R.id.addfriend);
    //Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/abc.ttf");
    //tx.setTypeface(custom_font);
    public AddFriend() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_friend, container, false);
    }

    public void getFriendsList(){

    }
}
