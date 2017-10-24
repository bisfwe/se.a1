package ch.ethz.inf.se.a1.smartenergy;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Andres on 15.10.17.
 */

public class Aktivitaet {

    private long durationInMillies;
    private static float metersTravelled;
    private int detectedActivityType;
    private double co2produced;
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
            case 0: return co2Vehicle();

            case 1: return co2Bicycle();

            // Still isn't producing co2..
            case 3: return 0.0;

            case 7: return co2Walking();

            case 8: return co2Running();

            // Tilting isn't producing co2..
            case 5: return  0.0;

            case 2: return co2Walking();

            default: return 0.0;
        }
    }

    private double co2Vehicle(){
        // TODO distinguish between different modes of transportation...
        // TODO calculate
        return 0.0;
    }

    private double co2Bicycle(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int lifestyle = pref.getInt("pref_lifestyle", 2);
        switch (lifestyle){
            case SettingsActivity.LIFESTYLE_MEET_LOVER:
                return Utils.meatLoverCycling/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_AVERAGE:
                return Utils.averageCycling/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_NO_BEEF:
                return Utils.noBeefCycling/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_VEGAN:
                return Utils.veganCycling/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_VEGETARIAN:
                return Utils.vegetarianCycling/1000 * metersTravelled;
            default:
                return Utils.averageCycling/1000 * metersTravelled;
        }
    }
    private double co2Walking(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int lifestyle = pref.getInt("pref_lifestyle", 2);
        switch (lifestyle){
            case SettingsActivity.LIFESTYLE_MEET_LOVER:
                return Utils.meatLoverWalking/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_AVERAGE:
                return Utils.averageWalking/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_NO_BEEF:
                return Utils.noBeefWalking/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_VEGAN:
                return Utils.veganWalking/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_VEGETARIAN:
                return Utils.vegetarianWalking/1000 * metersTravelled;
            default:
                return Utils.averageWalking/1000 * metersTravelled;
        }
    }
    private double co2Running(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int lifestyle = pref.getInt("pref_lifestyle", 2);
        switch (lifestyle){
            case SettingsActivity.LIFESTYLE_MEET_LOVER:
                return Utils.meatloverRunning/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_AVERAGE:
                return Utils.averageRunning/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_NO_BEEF:
                return Utils.noBeefRunning/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_VEGAN:
                return Utils.veganRunning/1000 * metersTravelled;
            case SettingsActivity.LIFESTYLE_VEGETARIAN:
                return Utils.vegetarianRunning/1000 * metersTravelled;
            default:
                return Utils.averageRunning/1000 * metersTravelled;
        }
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
