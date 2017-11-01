package ch.ethz.inf.se.a1.smartenergy;

import android.Manifest;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import ch.ethz.inf.se.a1.smartenergy.introfragments.CarUsage;
import ch.ethz.inf.se.a1.smartenergy.introfragments.HeatingType;
import ch.ethz.inf.se.a1.smartenergy.introfragments.HouseType;
import ch.ethz.inf.se.a1.smartenergy.introfragments.Lifestyle;
import ch.ethz.inf.se.a1.smartenergy.introfragments.LivingArea;
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
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name), getString(R.string.welcome), R.drawable.folder, getResources().getColor(R.color.colorPrimary)));
//        addSlide(AppIntroFragment.newInstance("Smart Energy", getString(R.string.permission_storage), R.drawable.folder, getResources().getColor(R.color.orange)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name), getString(R.string.permission_location), R.drawable.location, getResources().getColor(R.color.turkey)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name), getString(R.string.permission_internet), R.drawable.ic_network_wifi_white_48dp, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name), getString(R.string.going_settings), R.drawable.ic_settings_white_48dp, getResources().getColor(R.color.violet)));
        addSlide(new TransportationModes());
        addSlide(new CarUsage());
        addSlide(new Lifestyle());
        addSlide(new LivingArea());
        addSlide(new HouseType());
        addSlide(new HeatingType());
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name), getString(R.string.all_set), R.drawable.folder, Color.parseColor("#653BB5")));

        // Add permission request to the according slides
//        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        askForPermissions(new String[]{Manifest.permission.INTERNET}, 3);

        showSkipButton(false); // intro has to be done
        setProgressButtonEnabled(true); // see how far you are in the intro
        getSupportActionBar().hide(); // do not show the action bar

        setFadeAnimation();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        // Keep in mind that the intro was done successfully and has never to be done again
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = pref.edit();
        ed.putBoolean("introDone", true);
        ed.commit();

        // close this activity, will go back to the MainActivity and one can not come back
        this.finish();
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
