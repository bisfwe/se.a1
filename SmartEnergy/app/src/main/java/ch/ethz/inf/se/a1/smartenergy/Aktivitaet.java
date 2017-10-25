package ch.ethz.inf.se.a1.smartenergy;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.location.DetectedActivity;

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

    //first Update has to be recorded before second Update
    public Aktivitaet(Update first, Update second, Context context) {
        assert(first.getActivityType() == second.getActivityType());
        assert (first.getTime() < (second.getTime()));

        durationInMillies = second.getTime() - first.getTime();
        metersTravelled = first.getLocation().distanceTo(second.getLocation());
        detectedActivityType = first.getActivityType();
        co2produced = calculateCo2();

    }

    private double calculateCo2() {
        double result = 0.0;
        switch (detectedActivityType){
            case DetectedActivity.IN_VEHICLE: return co2Vehicle();

            case DetectedActivity.ON_BICYCLE: return co2Bicycle();

            // Still isn't producing co2..
            case DetectedActivity.STILL: return 0.0;

            case DetectedActivity.WALKING: return co2Walking();

            case DetectedActivity.RUNNING: return co2Running();

            // Tilting isn't producing co2..
            case DetectedActivity.TILTING: return  0.0;

            case DetectedActivity.ON_FOOT: return co2Walking();

            default: return 0.0;
        }
    }

    private double co2Vehicle(){
        double result = 0.0;
        // TODO distinguish between different modes of transportation...
        // TODO calculate
        redCo2 += result;
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
            case SettingsActivity.LIFESTYLE_AVERAGE:
                result += Utils.averageWalking/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_NO_BEEF:
                result += Utils.noBeefWalking/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_VEGAN:
                result += Utils.veganWalking/1000 * metersTravelled;
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
