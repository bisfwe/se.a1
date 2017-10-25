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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    protected static final String TAG = "MainActivity";
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private static final int INITIAL_REQUEST=1337;

    private Context mContext;
    private ListView mListView;

    private ArrayList<Day> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        // Show intro the very first time the app is opened
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this); //getSharedPreferences("IntroPreferences", Context.MODE_PRIVATE);
        if (!pref.getBoolean("introDone", false)) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivityForResult(intent, 0);
        }

        //check location permissions right at the start
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            checkPermission();
        }

        setContentView(R.layout.activity_main);

        ArrayList<DetectedActivity> detectedActivities = Utils.detectedActivitiesFromJson(
                getDefaultSharedPreferences(this).getString(
                        Constants.KEY_DETECTED_ACTIVITIES, ""));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: place this somewhere better and update frequently
                allUpdatesToDays();
                for (Day d : days) {
                    Toast.makeText(mContext,
                            String.format ("Total Co2 on this day: %f", d.getTotalCo2()),
                            Toast.LENGTH_SHORT)
                            .show();
                }

                Snackbar.make(view, "Populating days... ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // ListView displaying stats

        mListView = (ListView) findViewById(R.id.stats_list_view);

        allUpdatesToDays();
        DayAdapter adapter = new DayAdapter(this, days);
        mListView.setAdapter(adapter);

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

        /*
        if (!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }

        */

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            // got to list view
            Intent intent = new Intent(this, GraphActivity.class);
            String message = "Graph activity";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            //go to settings
            Intent intent = new Intent(this, SettingsActivity.class);
            String message = "test_message";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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

    public void showGraphView(View view){
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }

    //populates the local object days with all recorded updates ever
    private void allUpdatesToDays() {
        days = new ArrayList<Day>();

        ArrayList<Update> allUpdatesEver = UpdateService.loadAllUpdates(mContext);

        //process all updates, day by day
        ArrayList <Update> currentDayUpdates = new ArrayList<Update>();
        Calendar currentDate = null;
        for (Update u : allUpdatesEver) {
            if (currentDate == null) {
                currentDate = Calendar.getInstance();
                currentDate.setTime(new Date(u.getTime()));
            }
            Calendar updateDate = Calendar.getInstance();
            updateDate.setTime(new Date(u.getTime()));

            if (    currentDate.get(Calendar.YEAR) == updateDate.get(Calendar.YEAR) &&
                    currentDate.get(Calendar.MONTH) == updateDate.get(Calendar.MONTH) &&
                    currentDate.get(Calendar.DAY_OF_MONTH) == updateDate.get(Calendar.DAY_OF_MONTH)
                    ) {
                currentDayUpdates.add(u);

            }
            else {
                days.add(new Day(currentDayUpdates, this));
                currentDayUpdates = new ArrayList<Update>();
                currentDayUpdates.add(u);
                currentDate = updateDate;

            }
        }

        days.add(new Day(currentDayUpdates, this));
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

    // Creates dummy data
    public ArrayList<Day> createDummyData(){
        ArrayList<Day> result = new ArrayList<Day>();
        return result;

    }
}
