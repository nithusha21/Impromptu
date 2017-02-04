package com.codebreakers.apps.impromptu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    RestaurantFinder tester = new RestaurantFinder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        //tester.findRestaurants(12.9845,80.2330);
        initUI();

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
    private void initUI()
    {
        registerLink = (TextView) findViewById( R.id.registerLink );
        identityField = (EditText) findViewById( R.id.identityField );
        passwordField = (EditText) findViewById( R.id.passwordField );
        rememberLoginBox = (CheckBox) findViewById( R.id.rememberLoginBox );
        loginButton = (Button) findViewById( R.id.loginButton );
        String urlString = "https://api.backendless.com/v1/data/Users";
        try {
            URL url = new URL(urlString);

            getJSON task = new getJSON();
            task.execute(new URL(urlString));
        }catch (Exception e){
            e.printStackTrace();
        }
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
    private class getJSON extends AsyncTask<URL, Void, JSONObject>{
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

                System.out.println("JSON: " + jsonString);
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
