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
public class Update {

    public static Date time;
    public static Location location;
    public static ArrayList<DetectedActivity> activities;

    public Update(Date t, Location l, ArrayList<DetectedActivity> a){
        this.time = t;
        this.location = l;
        this.activities = a;
    }

}
