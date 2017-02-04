package com.codebreakers.apps.impromptu;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rajat on 04-02-2017.
 */

public class RestaurantFinder {

    private final static String ZOMATO_API_KEY = "8e847da3360a94cf26e381cbce62a41a",
            API_URL = "https://developers.zomato.com/api/v2.1/";
    private String resp;

    public void findRestaurants(double lat, double lon){
        URL reqURL;
        try{
            reqURL = new URL(API_URL+"geocode?lat="+lat+"&lon="+lon);
            new URLRequestTask().execute(reqURL);

        }catch(Exception e){
            Log.e("ERROR",e.getMessage());
        }
    }

    private class URLRequestTask extends AsyncTask<URL, Void, String> {
        private Exception exception;

        protected void onPreExecute() {
            Log.i("INFO","Pre Executing");
            resp = null;
        }

        protected String doInBackground(URL... urls) {

            try {
                URL url = urls[0];
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("user-key",ZOMATO_API_KEY);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);
            resp = response;
            //postRequestFunction();
        }
    }

}
