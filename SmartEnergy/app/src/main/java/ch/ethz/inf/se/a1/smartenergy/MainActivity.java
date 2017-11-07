package ch.ethz.inf.se.a1.smartenergy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    protected static final String TAG = "MainActivity";
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private static final int INITIAL_REQUEST=1337;

    private Context mContext;
    private ListView mListView;
    private TextView mHeatingCo2;
    private TextView mHeatingEnergy;

    private ArrayList<Day> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        // Show intro the very first time the app is opened
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (!pref.getBoolean("introDone", false)) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivityForResult(intent, 0);
        }

        //check location permissions right at the start
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            checkPermission();
        }

        setContentView(R.layout.activity_main);

        // ListView displaying stats
        mListView = (ListView) findViewById(R.id.stats_list_view);
        allUpdatesToDays();
        final DayAdapter adapter = new DayAdapter(this, days);
        mListView.setAdapter(adapter);

        //add heating information
        addHeatingInfo();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                allUpdatesToDays();
                adapter.notifyDataSetChanged();

                //add heating information
                addHeatingInfo();

                Snackbar.make(view, "Calculating your activities... ", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        // Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //only do that if you really want to delete all data!!!
        //UpdateService.destroyWholeUpdateList(mContext);

        //start the location/time/activity updates in the background
        //very important, otherwise we have no data to show
        if (canAccessLocation()){
            startService(new Intent(this, UpdateService.class));

        }

    }

    private void addHeatingInfo() {
        double heatingCo2 = calculateHeating();
        mHeatingCo2 = (TextView) findViewById(R.id.heating_co2);
        if (mHeatingCo2 != null) {
            mHeatingCo2.setText("" + Double.valueOf(heatingCo2).intValue());
        }
        double heatingEnergy = calculateEnergy();
        mHeatingEnergy = (TextView) findViewById(R.id.heating_energy);
        if (mHeatingEnergy != null) {
            mHeatingEnergy.setText("" + Double.valueOf(heatingEnergy).intValue());
        }
    }

    //method that asks the user for permission to turn on location
    //has to be checked everytime the app is created
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        allUpdatesToDays();

        //add heating information
        addHeatingInfo();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_info) {

            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_tutorial) {

            // start tutorial again
            Intent intent = new Intent(this, IntroActivity.class);
            startActivityForResult(intent, 0);

        } else if (id == R.id.nav_settings) {

           // switch to settings
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share this app");

            if (days.size() > 1) {

                Day currentDay = days.get(days.size()-1);
                String shareMessage = "With the App SmartEnergy, I can see how much CO2 I produce. Today I produced " + currentDay.getTotalCo2()+ " CO2. Isn't this fascinating? Check the app out and do something for your environment!";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Choose a messenger to share this App and your CO2 consumption!"));
            }
            else {
                String shareMessage = "With the App SmartEnergy, I can see how much CO2 I produce. Check it out as well and do something for your environment!";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Choose a messenger to share this App and your CO2 consumption!"));
            }


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Processes the list of freshly detected activities. Asks the adapter to update its list of
     * DetectedActivities with new {@code DetectedActivity} objects reflecting the latest detected
     * activities.
     */

    //populates the local object days with all recorded updates ever
    private void allUpdatesToDays() {
        days = new ArrayList<Day>();

        ArrayList<Update> allUpdatesEver = UpdateService.loadAllUpdates(mContext);

        if (allUpdatesEver.size() > 0) {
            //process all updates, day by day
            ArrayList<Update> currentDayUpdates = new ArrayList<Update>();
            Calendar currentDate = null;
            for (Update u : allUpdatesEver) {
                if (currentDate == null) {
                    currentDate = Calendar.getInstance();
                    currentDate.setTime(new Date(u.getTime()));
                }
                Calendar updateDate = Calendar.getInstance();
                updateDate.setTime(new Date(u.getTime()));

                if (currentDate.get(Calendar.YEAR) == updateDate.get(Calendar.YEAR) &&
                        currentDate.get(Calendar.MONTH) == updateDate.get(Calendar.MONTH) &&
                        currentDate.get(Calendar.DAY_OF_MONTH) == updateDate.get(Calendar.DAY_OF_MONTH)
                        ) {
                    currentDayUpdates.add(u);

                } else {
                    days.add(new Day(currentDayUpdates, this.getApplicationContext()));
                    currentDayUpdates = new ArrayList<Update>();
                    currentDayUpdates.add(u);
                    currentDate = updateDate;

                }
            }

            days.add(new Day(currentDayUpdates, this.getApplicationContext()));
        }
    }

    /**
     * calculate energy for heating per day
     * @return energy per day
     */
    private double calculateEnergy() {
        double result = calculateHeating();
        double heatingFactor;

        SharedPreferences pref = getDefaultSharedPreferences(mContext);
        String heatingType = pref.getString(getString(R.string.pref_key_heating_type), Integer.toString(SettingsActivity.HOME_OIL));

        switch (Integer.parseInt(heatingType)) {
            case SettingsActivity.HOME_GAS:
                heatingFactor = Utils.co2Gas;
                break;
            case SettingsActivity.HOME_ELECTRIC:
                heatingFactor = Utils.co2Electric;
                break;
            case SettingsActivity.HOME_AIR:
                heatingFactor = Utils.co2AirPump;
                break;
            case SettingsActivity.HOME_GROUND:
                heatingFactor = Utils.co2GroundPump;
                break;
            default:
                heatingFactor = Utils.co2Oil;
        }

        return result / heatingFactor;
    }

    // computes the heating stuff
    private double calculateHeating(){
        double result = 0.0;
        double houseFactor;
        double heatingFactor;
        SharedPreferences pref = getDefaultSharedPreferences(mContext);
        String houseType = pref.getString(getString(R.string.pref_key_age), Integer.toString(SettingsActivity.HOME_RENOVATED));
        switch (Integer.parseInt(houseType)) {
            case SettingsActivity.HOME_NEW:
                houseFactor = Utils.newBuilding;
                break;
            case SettingsActivity.HOME_OLD:
                houseFactor = Utils.oldBuilding;
                break;
            case SettingsActivity.HOME_MINERGIE:
                houseFactor = Utils.minergie;
                break;
            case SettingsActivity.HOME_P:
                houseFactor = Utils.minergieP;
                break;
            case SettingsActivity.HOME_A:
                houseFactor = Utils.minergieA;
                break;
            default:
                houseFactor = Utils.renovatedBuilding;
                break;
        }
        String heatingType = pref.getString(getString(R.string.pref_key_heating_type), Integer.toString(SettingsActivity.HOME_OIL));
        switch (Integer.parseInt(heatingType)) {
            case SettingsActivity.HOME_GAS:
                heatingFactor = Utils.co2Gas;
                break;
            case SettingsActivity.HOME_ELECTRIC:
                heatingFactor = Utils.co2Electric;
                break;
            case SettingsActivity.HOME_AIR:
                heatingFactor = Utils.co2AirPump;
                break;
            case SettingsActivity.HOME_GROUND:
                heatingFactor = Utils.co2GroundPump;
                break;
            default:
                heatingFactor = Utils.co2Oil;
        }
        String areaUsage = pref.getString(getString(R.string.pref_key_exact_area), "80");
        double area = Double.valueOf(areaUsage);
        result = area*heatingFactor*houseFactor;
        return result;

    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==ContextCompat.checkSelfPermission(MainActivity.this ,perm));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case INITIAL_REQUEST:
                if (canAccessLocation()) {

                } else {
                    // we are fucked, can't access loaction
                }
        }
    }

}
