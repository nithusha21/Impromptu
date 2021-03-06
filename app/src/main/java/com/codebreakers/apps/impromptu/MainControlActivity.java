package com.codebreakers.apps.impromptu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.backendless.Backendless;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainControlActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    static JSONArray jsonResults;
    static String user;
    private static int index = 0;
    private LocationManager locationManager;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main_control);

        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        user = prefs.getString("username", "UNKNOWN");
        URL urls[] = new URL[3];
        jsonResults = new JSONArray();
        try {
            urls[0] = new URL("https://api.backendless.com/v1/data/Users");
            urls[1] = new URL("https://api.backendless.com/v1/data/Friends");
            urls[2] = new URL("https://api.backendless.com/v1/data/Event");


            for(index = 0; index < 3; index++){
                new getJSON().execute(urls[index]);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            onLogoutButtonClicked();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        gps = new GPSTracker(MainControlActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.AddFriend) {
            fragment = new AddFriend();
        } else if (id == R.id.CreateEvent) {
            fragment = new createEvent();
        } else if (id == R.id.Settings) {
            fragment = new Settings();
        }
        if(fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main_control, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);



        return true;
    }

    private void onLogoutButtonClicked() {
        Backendless.UserService.logout( new DefaultCallback<Void>( this )
        {
            @Override
            public void handleResponse( Void response )
            {
                super.handleResponse( response );
                startActivity( new Intent( MainControlActivity.this, MainActivity.class ) );
                finish();
            }

            @Override
            public void handleFault( BackendlessFault fault )
            {
                if( fault.getCode().equals( "3023" ) ) // Unable to logout: not logged in (session expired, etc.)
                    handleResponse( null );
                else
                    super.handleFault( fault );
            }
        } );

    }


    private class getJSON extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls){


            try {
                HttpURLConnection urlConnection = (HttpURLConnection) urls[0].openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.setConnectTimeout(30000);
                urlConnection.setReadTimeout(30000);

                urlConnection.addRequestProperty("application-Id","48449D26-77AF-D84C-FFBC-96A6E338F700");
                urlConnection.addRequestProperty("secret-key","823D3BBA-94A9-3405-FFCE-D3F4BD1C1100");
                urlConnection.addRequestProperty("Content-type", "application/json");
                urlConnection.connect();
                if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.i("HTTP", "Failed");
                }
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                char[] buffer = new char[1024];



                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();

                String jsonString = sb.toString();

                index++;

                //Log.i("JSON: ",jsonString);
                return new JSONObject(jsonString);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                jsonResults.put(jsonObject);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(jsonObject != null)
                Log.i("Final Output",jsonObject.toString());
            /*try {
                JSONArray rows = jsonObject.getJSONArray("data"); // Get all JSONArray rows

                //  for(int i=0; i < rows.length(); i++) { // Loop over each each row
                //    JSONObject row = rows.getJSONObject(i); // Get row object
                //  JSONArray elements = row.getJSONArray("elements"); // Get all elements for each row as an array

                for(int j=0; j < rows.length(); j++) { // Iterate each element in the elements array
                    JSONObject element =  rows.getJSONObject(j); // Get the element object
                    JSONObject friendEmail = element.getJSONObject("friends"); // Get duration sub object


                    Log.i("Friends ",friendEmail.toString()); // Print int value

                }

            } catch (JSONException e) {
                // JSON Parsing error
                e.printStackTrace();
            }*/
        }
    }

    public static JSONObject getJsonUsers() {
        try {
            for(int i = 0; i < 3; i++){
                if(jsonResults.getJSONObject(i).getJSONArray("data").getJSONObject(0).getString("___class").equals("Users"))
                    return jsonResults.getJSONObject(i);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJsonEvents() {
        try {
            for(int i = 0; i < 3; i++){
                if(jsonResults.getJSONObject(i).getJSONArray("data").getJSONObject(0).getString("___class").equals("Event"))
                    return jsonResults.getJSONObject(i);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJsonFriends() {
        try {
            for(int i = 0; i < 3; i++){
                if(jsonResults.getJSONObject(i).getJSONArray("data").getJSONObject(0).getString("___class").equals("Friends"))
                    return jsonResults.getJSONObject(i);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getUser() {
        return user;
    }
}