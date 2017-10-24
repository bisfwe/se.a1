package ch.ethz.inf.se.a1.smartenergy;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Utility methods used in this sample.
 */
public class Utils {

    private Utils() {}

    /**
     * Returns a human readable String corresponding to a detected activity type.
     */
    static String getActivityString(Context context, int detectedActivityType) {
        Resources resources = context.getResources();
        switch(detectedActivityType) {
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                return resources.getString(R.string.unknown);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            default:
                return resources.getString(R.string.unidentifiable_activity, detectedActivityType);
        }
    }

    static String detectedActivitiesToJson(ArrayList<DetectedActivity> detectedActivitiesList) {
        Type type = new TypeToken<ArrayList<DetectedActivity>>() {}.getType();
        return new Gson().toJson(detectedActivitiesList, type);
    }

    static ArrayList<DetectedActivity> detectedActivitiesFromJson(String jsonArray) {
        Type listType = new TypeToken<ArrayList<DetectedActivity>>(){}.getType();
        ArrayList<DetectedActivity> detectedActivities = new Gson().fromJson(jsonArray, listType);
        if (detectedActivities == null) {
            detectedActivities = new ArrayList<>();
        }
        return detectedActivities;
    }

    static double meatLoverWalking = 29.91984799;
    static double meatLoverCycling = 74.79961998;
    static double meatloverRunning = 99.30920419;

    static double averageWalking = 22.66655151;
    static double averageCycling = 56.66637877;
    static double averageRunning = 75.2342456;

    static double noBeefWalking = 17.22657915;
    static double noBeefCycling = 43.06644787;
    static double noBeefRunning = 57.17802666;

    static double vegetarianWalking = 15.41325503;
    static double vegetarianCycling = 38.53313756;
    static double vegetarianRunning = 51.15928701;

    static double veganWalking = 13.59993091;
    static double veganCycling = 33.99982726;
    static double veganRunning = 45.14054736;

}