package ch.ethz.inf.se.a1.smartenergy;

import android.location.Location;

import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Andres on 13.10.17.
 */

//class to summarize the events that UpdateService gathers
    //each object has a static time, location and probability list of possible activities
public class Update implements Comparable<Update>{

    private final long timeInMillies;
    //save Location seperately such that it will not be a problem for gson to store them in sharedPreferences
    private final double longitude;
    private final double latitude;
    private final float accuracy;
    private final double altitude;
    private final int activityType;

    public Update(Date t, Location l, ArrayList<DetectedActivity> a){
        this.timeInMillies = t.getTime();
        this.longitude = l.getLongitude();
        this.latitude = l.getLatitude();
        this.altitude = l.getAltitude();
        this.accuracy = l.getAccuracy();
        this.activityType = getMostConfidentActivity(a).getType();
    }

    @Override
    public int compareTo(Update o) {
        return (int) (this.timeInMillies - (o.getTime()));
    }

    private DetectedActivity getMostConfidentActivity(ArrayList<DetectedActivity> activities) {
        DetectedActivity mostConfidentActivity = null;
        int highestConfidence = 0;
        for (DetectedActivity a : activities) {
            if (a.getConfidence() > highestConfidence) {
                mostConfidentActivity = a;
                highestConfidence = a.getConfidence();
            }
        }
        return mostConfidentActivity;
    }

    public int getActivityType(){
        return activityType;
    }

    public Location getLocation() {
        Location l = new Location("");
        l.setLongitude(longitude);
        l.setLatitude(latitude);
        l.setAltitude(altitude);
        l.setAccuracy(accuracy);
        return l;
    }

    public long getTime(){
        return timeInMillies;
    }

}
