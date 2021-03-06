package com.codebreakers.apps.impromptu;

import android.content.Intent;
import android.Manifest;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView registerLink;
    private EditText identityField, passwordField;
    private Button loginButton;
    private CheckBox rememberLoginBox;
    public static RestaurantFinder tester = new RestaurantFinder();
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        tester.findRestaurants(12.9867340,80.2239163);
        TextView tx = (TextView)findViewById(R.id.notauser);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/abc.ttf");

        tx.setTypeface(custom_font);
        //tester.findRestaurants(12.9845,80.2330);
        initUI();
        example();


        try {


            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                // execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        Backendless.setUrl( Defaults.SERVER_URL );
        Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

        Backendless.UserService.isValidLogin( new DefaultCallback<Boolean>( this )
        {
            @Override
            public void handleResponse( Boolean isValidLogin )
            {
                if( isValidLogin && Backendless.UserService.CurrentUser() == null )
                {
                    String currentUserId = Backendless.UserService.loggedInUser();

                    if( !currentUserId.equals( "" ) )
                    {
                        Backendless.UserService.findById( currentUserId, new DefaultCallback<BackendlessUser>( MainActivity.this, "Logging in..." )
                        {
                            @Override
                            public void handleResponse( BackendlessUser currentUser )
                            {
                                super.handleResponse( currentUser );
                                Backendless.UserService.setCurrentUser( currentUser );
                                startActivity( new Intent( getBaseContext(), MainControlActivity.class ) );
                                finish();
                            }
                        } );
                    }
                }

                super.handleResponse( isValidLogin );
            }
        });

    }

    private void example() {



        tester.findRestaurants(12.9917730,80.2321560);



    }

    private void initUI()
    {
        registerLink = (TextView) findViewById( R.id.registerLink );
        identityField = (EditText) findViewById( R.id.identityField );
        passwordField = (EditText) findViewById( R.id.passwordField );
        rememberLoginBox = (CheckBox) findViewById( R.id.rememberLoginBox );
        loginButton = (Button) findViewById( R.id.loginButton );
        String tempString = getResources().getString( R.string.register_text );
        SpannableString underlinedContent = new SpannableString( tempString );
        underlinedContent.setSpan( new UnderlineSpan(), 0, tempString.length(), 0 );
        registerLink.setText( underlinedContent );


        loginButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                onLoginButtonClicked();
            }
        } );

        registerLink.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                onRegisterLinkClicked();
            }
        } );



    }


    public void onLoginButtonClicked()
    {
        String identity = identityField.getText().toString();
        String password = passwordField.getText().toString();
        boolean rememberLogin = rememberLoginBox.isChecked();

        Backendless.UserService.login( identity, password, new DefaultCallback<BackendlessUser>( MainActivity.this )
        {
            public void handleResponse( BackendlessUser backendlessUser )
            {
                super.handleResponse( backendlessUser );
                SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                prefs.edit().putString("username", identityField.getText().toString().trim()).commit();
                Intent i = new Intent( MainActivity.this, MainControlActivity.class );
                startActivity(i);
                finish();
            }
        },rememberLogin );
    }

    public void onRegisterLinkClicked()
    {
        startActivity( new Intent( this, RegisterActivity.class ) );
        finish();
    }



}
