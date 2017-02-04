package com.codebreakers.apps.impromptu;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by Rajat on 04-02-2017.
 */

public class RestaurantFinder {

    private final static String ZOMATO_API_KEY = "8e847da3360a94cf26e381cbce62a41a",
            API_URL = "https://developers.zomato.com/api/v2.1/",
            urlDelimiter = "\nJSONURLSEPARATOR\n";
    private String resp;
    public JSONArray cuisines;
    public double[] lat, lon,
        priceForTwo,
        ratings;
    public int[] votes;
    public String[] names;
    public String[] restCuisines;

    private static final int MINIMUM_VOTES = 300;



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
                    return stringBuilder.toString()+urlDelimiter+url;
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
            postRequestFunction(response);
        }
    }

    private void postRequestFunction(String response){

        parseGeocodeResponse(response);
        Log.i("INFO", Arrays.toString(ratings));
        Log.i("INFO", Arrays.toString(votes));
        Log.i("INFO", Arrays.toString(priceForTwo));
        Log.i("INFO", Arrays.toString(names));
        Log.i("INFO", Arrays.toString(getWeightedRatings()));

    }

    private void parseGeocodeResponse(String response){
        String[] strs = response.split(urlDelimiter);
        String jsonReply = strs[0], url = strs[1];
//        Log.i("INFO",url);
        try {
            JSONObject restData = new JSONObject(jsonReply);
            cuisines = restData.getJSONObject("popularity").getJSONArray("top_cuisines");
            JSONArray rests = restData.getJSONArray("nearby_restaurants");

            int i=0;
            while(!rests.isNull(i)){
                i++;
            }
            int l = i;
            lat = new double[l];
            lon = new double[l];
            priceForTwo = new double[l];
            ratings = new double[l];
            votes = new int[l];
            names = new String[l];
            restCuisines = new String[l];
            i=0;
            while(!rests.isNull(i)){
                JSONObject restar = rests.getJSONObject(i).getJSONObject("restaurant");
                names[i] = restar.getString("name");
                restCuisines[i] = restar.getString("cuisines");
                lat[i] = restar.getJSONObject("location").getDouble("latitude");
                lon[i] = restar.getJSONObject("location").getDouble("longitude");
                priceForTwo[i] = restar.getDouble("average_cost_for_two");

                ratings[i] = restar.getJSONObject("user_rating").getDouble("aggregate_rating");
                votes[i] = restar.getJSONObject("user_rating").getInt("votes");

                i++;
            }

        } catch (JSONException e) {
            Log.e("ERROR",e.getMessage());
        }
    }


    private double[] getWeightedRatings(){
        int l = ratings.length;
        double[] wrs = new double[l];
        double s=0;
        for(int i=0;i<l;i++){
            s+=ratings[i];
        }
        double meanVote = s/l;

        for(int i=0;i<l;i++){
            int v =votes[i];
            wrs[i] = v/((double)v+MINIMUM_VOTES)*ratings[i] + MINIMUM_VOTES/((double)v+MINIMUM_VOTES)*meanVote;
        }
        return wrs;
    }

    public String[] rankRestaurants(int[] lats, int[] longs, String[] prefs){
        String[] rankList = new String[lats.length];
        return rankList;
    }

}
