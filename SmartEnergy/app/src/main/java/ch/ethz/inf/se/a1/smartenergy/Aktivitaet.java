package ch.ethz.inf.se.a1.smartenergy;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

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
    private int transportationMode;


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
            if (this.transportationMode == 0){
                determineTransportation();
                result = 0.0;
            } else if (this.transportationMode == 1){
                result = co2Tram();
            } else {
                result = co2Car();
            }
        } else if (detectedActivityType == 1){
            result = co2Bicycle();
        } else if (detectedActivityType == 7){
            result = co2Walking();
        } else if (detectedActivityType == 2){
            result = co2Walking();
        } else if (detectedActivityType == 8){
            result = co2Running();
        }
        return result;
    }
    // TODO calculate co2 for planes (activity probably unknown but mean speed rather high

    private void determineTransportation(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> selections = pref.getStringSet(context.getString(R.string.pref_key_used_transportation), null);
        // check for tram or train
        if (selections.contains("2")){
            String url = "http://transport.opendata.ch/v1/locations?x=" + start.getLongitude() + "&y=" + start.getLatitude();

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            handleResponse(response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.getMessage());
                        }
                    }
            );

// add it to the RequestQueue
            queue.add(getRequest);
        }
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

    private double co2Tram(){
        double result = metersTravelled/1000 * Utils.co2Tramway;
        yellowCo2 += result;
        return result;
    }

    private double co2Car(){
        double result;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean knowsUsage = pref.getBoolean(context.getString(R.string.pref_key_knows_usage),false);
        int fuelType = pref.getInt(context.getString(R.string.pref_key_fuel_type), SettingsActivity.TRANSPORTATION_PETROL);
        if (knowsUsage){
            double fuelConsumption = Double.valueOf(pref.getString(context.getString(R.string.pref_key_usage), "0.0"));
            if (fuelType == SettingsActivity.TRANSPORTATION_PETROL){
                double liters = metersTravelled*fuelConsumption/100000;
                result = liters * Utils.co2Petrol;
            } else {
                result = metersTravelled*fuelConsumption/100000*Utils.co2Diesel;
            }
        } else {
            double liters = 0.0;
            int carType = pref.getInt(context.getString(R.string.pref_key_car_type), SettingsActivity.TRANSPORTATION_MEDIUM_CAR);
            switch (carType) {
                case SettingsActivity.TRANSPORTATION_SMALL_CAR:
                    liters = metersTravelled*Utils.fuelConsumptionSmall/100000;
                    break;
                case SettingsActivity.TRANSPORTATION_BIG_CAR:
                    liters = metersTravelled*Utils.fuelConsumptionBig/100000;
                    break;
                default:
                    liters = metersTravelled*Utils.fuelConsumptionMedium/100000;
            }
            if (fuelType == SettingsActivity.TRANSPORTATION_PETROL){
                result = liters*Utils.co2Petrol;
            } else {
                result = liters*Utils.co2Diesel;
            }
        }
        redCo2 += result;
        return result;
    }

    private void handleResponse(JSONObject response){
        try {
            JSONObject station = response.getJSONArray("stations").getJSONObject(0);
            if (station.getInt("distance") < 20){
                // stands for tramway
                this.transportationMode = 1;
            } else {
                // stands for car
                this.transportationMode = 2;
            };
            // calculate co2 again after response
            this.co2produced = calculateCo2();
        } catch (JSONException e){
            e.printStackTrace();
        }
    };

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
