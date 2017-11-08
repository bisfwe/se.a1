package ch.ethz.inf.se.a1.smartenergy;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import java.util.Set;

import ch.ethz.inf.se.a1.smartenergy.introfragments.AllSet;
import ch.ethz.inf.se.a1.smartenergy.introfragments.CarUsage;
import ch.ethz.inf.se.a1.smartenergy.introfragments.HeatingType;
import ch.ethz.inf.se.a1.smartenergy.introfragments.HouseType;
import ch.ethz.inf.se.a1.smartenergy.introfragments.Lifestyle;
import ch.ethz.inf.se.a1.smartenergy.introfragments.LivingArea;
import ch.ethz.inf.se.a1.smartenergy.introfragments.TransportationModes;

import static ch.ethz.inf.se.a1.smartenergy.SettingsActivity.TRANSPORTATION_CAR;

/**
 * This is an activity for the intro slides which also requests the required permissions.
 * This intro is only shown a single time (the first time the app is opened).
 */

public class IntroActivity extends AppIntro2 {

    /**
     * tag for logging
     */
    private final static String TAG = "IntroActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add intro slides
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name), getString(R.string.welcome), R.drawable.folder, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name), getString(R.string.howItWorks), R.drawable.bars, getResources().getColor(R.color.violet)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name), getString(R.string.permission_location), R.drawable.location, getResources().getColor(R.color.turkey)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name), getString(R.string.permission_internet), R.drawable.ic_network_wifi_white_48dp, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("No Internet Detected!", "Please insert a SIM card. Without an active Internet connection, the app will not work.", R.drawable.ic_sim_card_alert_white_48dp, getResources().getColor(R.color.turkey)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name), getString(R.string.going_settings), R.drawable.ic_settings_white_48dp, getResources().getColor(R.color.violet)));
        addSlide(new TransportationModes());
        addSlide(new CarUsage());
        addSlide(new Lifestyle());
        addSlide(new LivingArea());
        addSlide(new HouseType());
        addSlide(new HeatingType());
        addSlide(new AllSet());

        // Add permission request to the according slides
        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
        askForPermissions(new String[]{Manifest.permission.INTERNET}, 4);

        showSkipButton(false); // intro has to be done
        setProgressButtonEnabled(true); // see how far you are in the intro
        getSupportActionBar().hide(); // do not show the action bar
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

    private boolean simChecked = false;

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        Log.i(TAG, "slide change");

        // skip car usage slide if the user does not drive cars
        try {
            if (newFragment != null && newFragment.toString().startsWith(CarUsage.class.getName().substring(45))) {
                // car usage slide is accessed
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                Set transportationModes = pref.getStringSet(getString(R.string.pref_key_used_transportation), null);

                if (transportationModes != null && !transportationModes.toString().contains(Integer.toString(TRANSPORTATION_CAR))) {
                    // the user is not driving cars
                    if (oldFragment.toString().startsWith(TransportationModes.class.getName().substring(45))) {
                        // coming from left
                        ((ViewGroup) getSlides().get(6).getView().getParent().getParent()).findViewById(R.id.next).performClick();
                    } else if (oldFragment.toString().startsWith(Lifestyle.class.getName().substring(45))) {
                        // coming from right
                        onBackPressed();
                    }
                }
            }

            if (!simChecked && ((ViewGroup) getSlides().get(5).getView()) != null) {
                TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                int simState = telMgr.getSimState();
                simChecked = true;
                switch (simState) {
                    case TelephonyManager.SIM_STATE_ABSENT:
                    case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                    case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    case TelephonyManager.SIM_STATE_UNKNOWN:
                    default:
                        break;
                    case TelephonyManager.SIM_STATE_READY:
                        ((ViewGroup) getSlides().get(4).getView().getParent().getParent()).findViewById(R.id.next).performClick();
                        break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage()); // can happen if the user turns the screen exactly before this code is executed
        }
    }

    @Override
    public void onBackPressed() {
        // do not allow to go back on the first intro slides, intro should be done
        if (getSlides().get(0).getView() == null) {
            super.onBackPressed();
        }
    }
}
