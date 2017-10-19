package ch.ethz.inf.se.a1.smartenergy;

/**
 * Created by Andres on 13.10.17.
 */

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class UpdateService extends Service
{
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    //minimum time in milliseconds
    private static final int LOCATION_INTERVAL = 1000;
    //minimum distance to upate in meters
    private static final float LOCATION_DISTANCE = 2;
    //object that can send newest activity probabilities
    private ActivityRecognitionClient mActivityRecognitionClient;


    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        //this method will be automatically triggered once the current time or location
        //is higher than LOCATION_INTERVAL or LOCATION_DISTANCE
        public void onLocationChanged(final Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            storeUpdateWithLocation(location);

        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        //creating new activity client that will send activity probabilities after a new location is found
        mActivityRecognitionClient = new ActivityRecognitionClient(this);

        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(Constants.DETECTION_INTERVAL_IN_MILLISECONDS, getActivityDetectionPendingIntent());

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void result) {
            Log.w(TAG, getString(R.string.activity_updates_enabled));
        }});

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, getString(R.string.activity_updates_not_enabled));
            }
        });

    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    /**
     * Gets a PendingIntent to be sent for each activity detection.
     */
    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //save the new Update to the shared preferences
    //can be retrieved from any activity with the correct tag
    //read out the string with gson
    private static void saveNewUpdate(Update u, Context c) {
        //load previous updates
        ArrayList<Update> updateList = loadAllUpdates(c);
        //add new update to the list
        updateList.add(u);

        Log.e(TAG, "UpdateList now looks like:  " + updateList);

        SharedPreferences sharedPrefs = getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        //convert the list to a json
        Gson gson = new Gson();
        String json = gson.toJson(updateList);

        //save it forever
        editor.putString(Constants.UPDATE_STORE_TAG, json);
        editor.commit();

    }

    //you can use this to get updates in other activities
    //just use it like:
    // ArrayList<Update> previousStoredUpdates = UpdateService.loadAllUpdates();
    public static ArrayList<Update> loadAllUpdates(Context c){
        SharedPreferences sharedPrefs = getDefaultSharedPreferences(c);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.UPDATE_STORE_TAG, null);
        if (json == null || json == "") {
            //this is the first time anyone has requested a list
            //just send an empty list
            return new ArrayList<Update>();

        }
        else {
            Type type = new TypeToken<ArrayList<Update>>() {}.getType();
            return gson.fromJson(json, type);

        }

    }

    public void storeUpdateWithLocation(Location location) {

        final Date currentTime = Calendar.getInstance().getTime();
        Log.e(TAG, "currentTime: " + currentTime);

        //TODO: somehow these activities always return STILL with 100% confidence
        ArrayList<DetectedActivity> detectedActivities = Utils.detectedActivitiesFromJson(
                getDefaultSharedPreferences(getApplicationContext())
                        .getString(Constants.KEY_DETECTED_ACTIVITIES, ""));

        Log.e(TAG, "Activities: " + detectedActivities);

        saveNewUpdate(new Update(currentTime, location, detectedActivities), this);


    }

    //ATTENTION: do not use this method in final release
    //just for debugging if you change the implementation of Update and can't read out the old Update objects
    /*
    public static void destroyWholeUpdateList(Context c) {

        //override with empty list
        ArrayList<Update> updateList = new ArrayList<Update>();
        Log.e(TAG, "UpdateList now looks like:  " + updateList);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        //convert the list to a json
        Gson gson = new Gson();
        String json = gson.toJson(updateList);

        //save it forever
        editor.putString(Constants.UPDATE_STORE_TAG, json);
        editor.commit();

    }
    */




}
