package ch.ethz.inf.se.a1.smartenergy;

/**
 * Created by Andres on 15.10.17.
 */

import android.content.Context;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Day {

    public Calendar date;
    public ArrayList<Aktivitaet> aktivitaetenList;
    private Context context;

    //initialize Day with a list of updates that have to be all on the same day
    public Day(ArrayList<Update> updates, Context context) {
        this.context = context;

        //sort the updates in ascending time
        Collections.sort(updates);

        //set date for this Day
        //ensure that all updates are at same date
        for (Update u : updates) {
            if (date == null) {
                date = Calendar.getInstance();
                date.setTime(new Date(u.getTime()));
            }
            else {
                Calendar otherDate = Calendar.getInstance();
                otherDate.setTime(new Date(u.getTime()));
                assert( date.get(Calendar.YEAR) == otherDate.get(Calendar.YEAR) &&
                        date.get(Calendar.MONTH) == otherDate.get(Calendar.MONTH) &&
                        date.get(Calendar.DAY_OF_MONTH) == otherDate.get(Calendar.DAY_OF_MONTH)
                );
            }
        }

        //populate the aktivitaetenList
        aktivitaetenList = new ArrayList<Aktivitaet>();
        if (updates.size() > 1) {
            Update startOfActivity = updates.get(0);
            for (int i = 1; i < updates.size(); i++) {
                Update currentUpdate = updates.get(i);
                if (currentUpdate.getActivityType() == startOfActivity.getActivityType()) {

                    //check if the next update is a continuation of this current activity:
                    //if it is, ignore this update
                    if (i+1 < updates.size()) {
                        if (updates.get(i+1).getActivityType() == currentUpdate.getActivityType()) {
                            continue;
                        }
                    }
                    aktivitaetenList.add(new Aktivitaet(startOfActivity, currentUpdate, context));
                    if (i< updates.size()-1){
                        startOfActivity = updates.get(i+1);
                    }
                }
            }
        }
    }

    public double getTotalCo2() {
        double total = 0.0;
        for (Aktivitaet a : aktivitaetenList) {
            total += a.getCo2produced();
        }
        return total;
    }

}
