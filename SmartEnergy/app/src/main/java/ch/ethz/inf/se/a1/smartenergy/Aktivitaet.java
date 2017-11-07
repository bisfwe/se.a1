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
    private double energyUsed;
    private Context context;
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

    private double determineTransportation(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> selections = pref.getStringSet(context.getString(R.string.pref_key_used_transportation), null);
        if (selections == null || selections.size() <= 0){
            return 0.0;
        }
        // collections contains car and tram or train, we use the transport api to determine which of them it is..
        if (selections.contains("3") && (selections.contains("2") || selections.contains("4"))){
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
            // add it to the RequestQueue, response is handled by handleResponse()
            queue.add(getRequest);
            return 0.0;
        } else if (!selections.contains("3") && (selections.contains("2") || selections.contains(("4")))){
            // only public transport selected, can be computed directly
            this.transportationMode = 1;
            return co2Tram();
        } else if(selections.contains("3") && !(selections.contains("2") || selections.contains(("4")))){
            // only car selcted, no public transport
            this.transportationMode = 2;
            return co2Car();
        }
        return 0.0;
    }

    // calculate co2 for bycicle aktivitaet
    private double co2Bicycle(){
        double result = 0.0;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int lifestyle = Integer.valueOf(pref.getString("pref_lifestyle", String.valueOf(2)));
        switch (lifestyle){
            case SettingsActivity.LIFESTYLE_MEAT_LOVER:
                result += Utils.meatLoverCycling/1000 * metersTravelled;
                break;
            case SettingsActivity.LIFESTYLE_AVERAGE:
                result += Utils.averageCycling/1000 * metersTravelled;
                break;
            case SettingsActivity.LIFESTYLE_NO_BEEF:
                result += Utils.noBeefCycling/1000 * metersTravelled;
                break;
            case SettingsActivity.LIFESTYLE_VEGAN:
                result += Utils.veganCycling/1000 * metersTravelled;
                break;
            case SettingsActivity.LIFESTYLE_VEGETARIAN:
                result += Utils.vegetarianCycling/1000 * metersTravelled;
                break;
            default:
                result += Utils.averageCycling/1000 * metersTravelled;
        }
        greenCo2 += result;
        energyUsed = (metersTravelled/1000) * Utils.energyCycling;
        return result;
    }
    //calculates co2 for walking aktivitaet
    private double co2Walking(){
        double result = 0.0;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int lifestyle = Integer.valueOf(pref.getString("pref_lifestyle", String.valueOf(2)));
        switch (lifestyle){
            case SettingsActivity.LIFESTYLE_MEAT_LOVER:
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
        energyUsed = (metersTravelled/1000) * Utils.energyWalking;
        return result;
    }
    // calculates co2 for running aktivitaet
    private double co2Running(){
        double result = 0.0;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int lifestyle = Integer.valueOf(pref.getString("pref_lifestyle", String.valueOf(2)));
        switch (lifestyle){
            case SettingsActivity.LIFESTYLE_MEAT_LOVER:
                result += Utils.meatloverRunning/1000 * metersTravelled;
                break;
            case SettingsActivity.LIFESTYLE_AVERAGE:
                result += Utils.averageRunning/1000 * metersTravelled;
                break;
            case SettingsActivity.LIFESTYLE_NO_BEEF:
                result += Utils.noBeefRunning/1000 * metersTravelled;
                break;
            case SettingsActivity.LIFESTYLE_VEGAN:
                result += Utils.veganRunning/1000 * metersTravelled;
                break;
            case SettingsActivity.LIFESTYLE_VEGETARIAN:
                result += Utils.vegetarianRunning/1000 * metersTravelled;
                break;
            default:
                result += Utils.averageRunning/1000 * metersTravelled;
        }
        greenCo2 += result;
        energyUsed = (metersTravelled/1000) * Utils.energyRunning;
        return result;
    }

    // calculates co2 for tram/public transport
    private double co2Tram(){
        double result = metersTravelled/1000 * Utils.co2Tramway;
        yellowCo2 += result;
        energyUsed = (metersTravelled/1000) * Utils.energyTram;
        return result;
    }

    //calculates co2 for driving car aktivitaet
    private double co2Car(){
        double result;
        double energy;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean knowsUsage = pref.getBoolean(context.getString(R.string.pref_key_knows_usage),false);
        int fuelType = Integer.valueOf(pref.getString(context.getString(R.string.pref_key_fuel_type), String.valueOf(SettingsActivity.TRANSPORTATION_PETROL)));
        if (knowsUsage){
            double fuelConsumption = Double.valueOf(pref.getString(context.getString(R.string.pref_key_usage), "0.0"));
            double liters = metersTravelled*fuelConsumption/100000;
            if (fuelType == SettingsActivity.TRANSPORTATION_PETROL){
                result = liters * Utils.co2Petrol;
                energy = liters * Utils.energyPetrol;
            } else {
                result = liters * Utils.co2Diesel;
                energy = liters * Utils.energyDiesel;
            }
        } else {
            double liters = 0.0;
            int carType = Integer.valueOf(pref.getString(context.getString(R.string.pref_key_car_type), String.valueOf(SettingsActivity.TRANSPORTATION_MEDIUM_CAR)));
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
                result = liters * Utils.co2Petrol;
                energy = liters * Utils.energyPetrol;
            } else {
                result = liters* Utils.co2Diesel;
                energy = liters * Utils.energyDiesel;
            }
        }
        redCo2 += result;
        energyUsed = energy;
        return result;
    }

    // handles response of swiss public transport api
    private void handleResponse(JSONObject response){
        try {
            JSONObject station = response.getJSONArray("stations").getJSONObject(0);
            if (station.getInt("distance") < 20){
                // stands for tramway
                this.transportationMode = 1;
            } else {
                // stands for car
                this.transportationMode = 2;
            }
            // calculate co2 again after response
            this.co2produced = calculateCo2();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public double getCo2produced() {
        return co2produced;
    }
    public double getGreenCo2() { return greenCo2; }
    public double getRedCo2() { return redCo2; }
    public double getYellowCo2() { return yellowCo2; }
    public double getEnergyUsed() { return energyUsed; }
}
