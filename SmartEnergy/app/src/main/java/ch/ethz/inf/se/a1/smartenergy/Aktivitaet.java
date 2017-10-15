package ch.ethz.inf.se.a1.smartenergy;

/**
 * Created by Andres on 15.10.17.
 */

public class Aktivitaet {

    private long durationInMillies;
    private static float metersTravelled;
    private int detectedActivityType;
    private double co2produced;

    //first Update has to be recorded before second Update
    public Aktivitaet(Update first, Update second) {
        assert(first.getActivityType() == second.getActivityType());
        assert (first.getTime() < (second.getTime()));

        durationInMillies = second.getTime() - first.getTime();
        metersTravelled = first.getLocation().distanceTo(second.getLocation());
        detectedActivityType = first.getActivityType();
        co2produced = calculateCo2();

    }

    private double calculateCo2() {

        //TODO: calculate based on durationInMillies, metersTravelled, detectedActivityType

       return 0.0;
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
