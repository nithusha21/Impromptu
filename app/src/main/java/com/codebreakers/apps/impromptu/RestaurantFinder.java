package com.codebreakers.apps.impromptu;

import android.location.Location;
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
    double[] latAnswers, lonAnswers;

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
        double[] laats = {12.991773,13,12.98}, loons = {80.232156,80.21,80.21};
        String[] prefs = {"","",""};
        double[] price = {500,350,750};
////        Log.i("INFO", Arrays.toString(ratings));
////        Log.i("INFO", Arrays.toString(votes));
        Log.i("INFO", Arrays.toString(priceForTwo));
//        Log.i("INFO", Arrays.toString(restCuisines));
////        Log.i("INFO", Arrays.toString(getWeightedRatings()));
        Log.i("old list", Arrays.toString(names));
        Log.i("final rank list", Arrays.toString((rankRestaurants(laats,loons,prefs,price))));
        Log.i("length",(rankRestaurants(laats,loons,prefs,price)).length+"");

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

    public String[] rankRestaurants(double[] lats, double[] lons, String[] prefs, double[] pricePrefs){
        int numRests = lat.length;
        int numPpl = prefs.length;
        String[] rankList = new String[numRests];

        double[] wrs = getWeightedRatings();
        normalize(wrs);

        double[] dists= new double[numRests];
        for(int i=0;i<numRests;i++){
            double x=lat[i], y=lon[i];

            for(int j=0;j<numPpl;j++){
                dists[i]+=Math.pow(x-lats[j],2)+Math.pow(y-lons[j],2);
            }
            Log.i("lat[i],lon[i]",lat[i]+","+lon[i]);
            dists[i] = 1/dists[i];
        }
        normalize(dists);
//        for(int i=0;i<numRests;i++){
//            dists[i] = 1-dists[i];
//        }

        double[] priceHappyPpl = new double[numRests];
        for(int i=0;i<numRests;i++){
            for(int j=0;j<numPpl;j++){
                if(priceForTwo[i]<pricePrefs[j]*2)priceHappyPpl[i]++;
            }
        }
        Log.i("Price happy ppl",Arrays.toString(priceHappyPpl));
        normalize(priceHappyPpl);
        Log.i("Price happy ppl",Arrays.toString(priceHappyPpl));

        double[] prefHappyPpl = new double[numRests];
        for(int i=0;i<numRests;i++){
            String[] cu = restCuisines[i].split(",");
            for(int j=0;j<numPpl;j++){
                String[] prefcu = prefs[j].split(",");
                outer:for(int p=0;p<cu.length;p++){
                    for(int q=0;q<prefcu.length;q++){
                        if(cu[p].trim().equals(prefs[j].trim())){
                            prefHappyPpl[i]++;
                            break outer;
                        }
                    }
                }
            }
        }


        normalize(prefHappyPpl);
        Log.i("pref happy ppl", Arrays.toString(prefHappyPpl));
        Log.i("wrs", Arrays.toString(wrs));
        Log.i("price happy ppl", Arrays.toString(priceHappyPpl));
        Log.i("dists", Arrays.toString(dists));
        double[] restGoodnessIndex = new double[numRests];
        for(int i=0;i<numRests;i++){
            restGoodnessIndex[i] = dists[i]+prefHappyPpl[i]+priceHappyPpl[i]+wrs[i];
        }

        Log.i("restGoodnessIdiec", Arrays.toString(restGoodnessIndex));

        int index=0;
        double min = restGoodnessIndex[0];
        double largestSorted=0;
        latAnswers = new double[numRests];
        lonAnswers = new double[numRests];
        for(int i=0;i<numRests;i++){
            for(int j=0;j<numRests;j++){
                if(restGoodnessIndex[j]<=largestSorted)continue;
                if(restGoodnessIndex[j]<min){
                    min = restGoodnessIndex[j];
                    index = j;
                }
            }
            rankList[i] = names[index];
            latAnswers[i] = lat[index];
            lonAnswers[i] = lon[index];
            largestSorted = min;
            min = Integer.MAX_VALUE;
        }

        String[] rankListReversed = new String[numRests];
        for(int i=0;i<numRests;i++){
            rankListReversed[i] = rankList[numRests-i-1];
        }

        return rankListReversed;
    }

    private void normalize(double[] arr){
        double s=0;
        for(int i=0;i<arr.length;i++){
            s+=arr[i];
        }
        if(s==0)return;
        for(int i=0;i<arr.length;i++){
            arr[i]/=s;
        }
    }



    public Location[] getRestaurantLocations(){
        Location[] locs = new Location[lat.length];
        for(int i=0;i<locs.length;i++){
            locs[i] = new Location("");
            locs[i].setLatitude(lat[i]);
            locs[i].setLongitude(lon[i]);
        }
        return locs;
    }

}
