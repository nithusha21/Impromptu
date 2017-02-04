package com.codebreakers.apps.impromptu;

import android.content.Intent;
import android.content.SharedPreferences;
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
        tester.findRestaurants(12.9845,80.2330);
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

        try{
            JSONObject jsonObject = getJSONObjectFromURL(urlString);
            String name[];
            Log.i("JSON", jsonObject.toString());
            // Parse your json here
            // JSONObject sys  = jsonObject.getJSONObject("data");


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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


    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        URL url = new URL(urlString);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("application-Id", "48449D26-77AF-D84C-FFBC-96A6E338F700");
        urlConnection.setRequestProperty("secret-key","48449D26-77AF-D84C-FFBC-96A6E338F700");
        //urlConnection.setReadTimeout(10000 );
        //urlConnection.setConnectTimeout(15000);

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
