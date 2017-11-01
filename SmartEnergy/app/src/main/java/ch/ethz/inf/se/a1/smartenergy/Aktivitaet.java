package ch.ethz.inf.se.a1.smartenergy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.preference.PreferenceManager;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONObject;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Andres on 15.10.17.
 */

public class Aktivitaet {

    private long durationInMillies;
    private static float metersTravelled;
    private int detectedActivityType;
    private double co2produced;
    // co2 produced by running, walking..
    private double greenCo2;
    // co2 produced by car, plane
    private double redCo2;
    // co2 produced by public transport, these are displayed in a graph in the list view. not implemented yet..
    private double yellowCo2;
    private Context context;
    private RequestQueue mQueue;
    RequestQueue mRequestQueue;
    Location start;
    Location end;


    //first Update has to be recorded before second Update
    public Aktivitaet(Update first, Update second, Context context) {
        this.context = context;
        assert(first.getActivityType() == second.getActivityType());
        assert (first.getTime() < (second.getTime()));

        durationInMillies = second.getTime() - first.getTime();
        metersTravelled = first.getLocation().distanceTo(second.getLocation());
        start = first.getLocation();
        end = second.getLocation();
        detectedActivityType = first.getActivityType();
        co2produced = calculateCo2();

    }

    private double calculateCo2() {
        double result = 0.0;
        if (detectedActivityType == 0){
            result = co2Vehicle();
        } else if (detectedActivityType == 1){
            result = co2Bicycle();
        } else if (detectedActivityType == 7){
            result = co2Walking();
        } else if (detectedActivityType == 2){
            result = co2Walking();
        }
        return result;
    }
    // TODO calculate co2 for planes (activity probably unknown but mean speed rather high

    private double co2Vehicle(){
        // Instantiate the cache
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        // Start the queue
        mRequestQueue.start();
        double result = 0.0;
       /* SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> selections = sharedPrefs.getStringSet(Resources.getSystem().getString(R.string.pref_key_used_transportation), null);
        // check for train
        if (selections.contains("2")){
            String requestURL = "http://transport.opendata.ch/v1/locations?x=" + start.getAltitude() + "&y=" + start.getLatitude();
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(requestURL, new JSONObject(), future, future);
            mRequestQueue.add(request);
            try {
                JSONObject response = future.get(2, TimeUnit.SECONDS);// this will block
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                // exception handling
            }

        }
        // TODO calculate
        redCo2 += result;*/
        return 0.0;
    }

    private double co2Bicycle(){
        double result = 0.0;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int lifestyle = pref.getInt("pref_lifestyle", 2);
        switch (lifestyle){
            case SettingsActivity.LIFESTYLE_MEET_LOVER:
                result += Utils.meatLoverCycling/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_AVERAGE:
                result += Utils.averageCycling/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_NO_BEEF:
                result += Utils.noBeefCycling/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_VEGAN:
                result += Utils.veganCycling/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_VEGETARIAN:
                result += Utils.vegetarianCycling/1000 * metersTravelled;
            default:
                result += Utils.averageCycling/1000 * metersTravelled;
        }
        greenCo2 += result;
        return result;
    }
    private double co2Walking(){
        double result = 0.0;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int lifestyle = pref.getInt("pref_lifestyle", 2);
        switch (lifestyle){
            case SettingsActivity.LIFESTYLE_MEET_LOVER:
                result += Utils.meatLoverWalking/1000 * metersTravelled;
                break;
            case SettingsActivity.LIFESTYLE_AVERAGE:
                result += Utils.averageWalking/1000 * metersTravelled;
                break;
            case SettingsActivity.LIFESTYLE_NO_BEEF:
                result += Utils.noBeefWalking/1000 * metersTravelled;
                break;
            case SettingsActivity.LIFESTYLE_VEGAN:
                result += Utils.veganWalking/1000 * metersTravelled;
                break;
            case SettingsActivity.LIFESTYLE_VEGETARIAN:
                result += Utils.vegetarianWalking/1000 * metersTravelled;
            default:
                result += Utils.averageWalking/1000 * metersTravelled;
        }
        greenCo2 += result;
        return result;
    }
    private double co2Running(){
        double result = 0.0;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int lifestyle = pref.getInt("pref_lifestyle", 2);
        switch (lifestyle){
            case SettingsActivity.LIFESTYLE_MEET_LOVER:
                result += Utils.meatloverRunning/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_AVERAGE:
                result += Utils.averageRunning/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_NO_BEEF:
                result += Utils.noBeefRunning/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_VEGAN:
                result += Utils.veganRunning/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_VEGETARIAN:
                result += Utils.vegetarianRunning/1000 * metersTravelled;
            default:
                result += Utils.averageRunning/1000 * metersTravelled;
        }
        greenCo2 += result;
        return result;
    }

    public long getDurationSeconds() {
        return durationInMillies;
    }
    public float getMetersTravelled() {
        return metersTravelled;
    }
    public int getDetectedActivityType(){
        return detectedActivityType;
    }
    public double getCo2produced() {
        return co2produced;
    }
}
