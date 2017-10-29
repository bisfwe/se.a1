package ch.ethz.inf.se.a1.smartenergy;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import ch.ethz.inf.se.a1.smartenergy.introfragments.TransportationModes;

/**
 * This is an activity for the intro slides which also request the required permissions.
 * This intro is only shown a single time (the first time the app is opened).
 */

public class IntroActivity extends AppIntro2 {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add intro slides
        addSlide(AppIntroFragment.newInstance("Smart Energy", "Hello, welcome to Smart Energy. This app starts a service in the background that keeps track of information related to your carbon dioxide footprint.", R.drawable.folder, Color.parseColor("#3F51B5")));

        addSlide(new TransportationModes());

        addSlide(AppIntroFragment.newInstance("Smart Energy", "In order to store the logged data, please allow access to the storage system. Otherwise, the app cannot work.", R.drawable.folder, Color.parseColor("#FFA441")));
        addSlide(AppIntroFragment.newInstance("Smart Energy", "In order to get your position, the app needs location access.", R.drawable.location, Color.parseColor("#3087A8")));
        addSlide(AppIntroFragment.newInstance("Smart Energy", "Now, we will go through a few settings on the next pages. You can also change them later by going to \"Settings\".", R.drawable.folder, Color.parseColor("#653BB5")));
        addSlide(AppIntroFragment.newInstance("Smart Energy", "Thanks, everything is set up.", R.drawable.folder, Color.parseColor("#653BB5")));

        // Add permission request to the according slides
        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 3);

        showSkipButton(false); // intro has to be done
        setProgressButtonEnabled(true); // see how far you are in the intro
        getSupportActionBar().hide(); // do not show the action bar


    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        // Keep in mind that the intro was done successfully and has never to be done again
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor ed = pref.edit();
//        ed.putBoolean("introDone", true);
//        ed.commit();

        // close this activity, will go back to the MainActivity and one can not come back
        this.finish();
    }

    @Override
    public void onBackPressed() {
        // do intentionally nothing, intro has to be done
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}
