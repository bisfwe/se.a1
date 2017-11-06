package ch.ethz.inf.se.a1.smartenergy;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * tag for logging
     */
    public static final String TAG = "SettingsActivity";

    // means of transportation constants
    public static final int TRANSPORTATION_AIRPLANE = 1;
    public static final int TRANSPORTATION_TRAIN = 2;
    public static final int TRANSPORTATION_CAR = 3;
    public static final int TRANSPORTATION_TRAM = 4;
    public static final int TRANSPORTATION_BICYCLE = 5;

    // car-related constants
    public static final int TRANSPORTATION_SMALL_CAR = 1;
    public static final int TRANSPORTATION_MEDIUM_CAR = 2;
    public static final int TRANSPORTATION_BIG_CAR = 3;
    public static final int TRANSPORTATION_PETROL = 1;
    public static final int TRANSPORTATION_DIESEL = 2;

    // lifestyle constants
    public static final int LIFESTYLE_MEAT_LOVER = 1;
    public static final int LIFESTYLE_AVERAGE = 2;
    public static final int LIFESTYLE_NO_BEEF = 3;
    public static final int LIFESTYLE_VEGETARIAN = 4;
    public static final int LIFESTYLE_VEGAN = 5;

    // heating system constants
    public static final int HOME_OIL = 1;
    public static final int HOME_GAS = 2;
    public static final int HOME_ELECTRIC = 3;
    public static final int HOME_AIR = 4;
    public static final int HOME_GROUND = 5;

    // housing size constants
    public static final int HOME_ROOM = 1;
    public static final int HOME_FLAT = 2;
    public static final int HOME_HOUSE = 3;
    public static final int HOME_VILLA = 4;

    // housing energy standard constants
    public static final int HOME_NEW = 1;
    public static final int HOME_RENOVATED = 2;
    public static final int HOME_OLD = 3;
    public static final int HOME_MINERGIE = 4;
    public static final int HOME_P = 5;
    public static final int HOME_A = 6;

    /**
     * selection list containing information about the used modes of transportation
     */
    private static MultiSelectListPreference usedTransportation;

    /**
     * list preferences telling which option the user has chosen
     */
    private static ListPreference carType, fuelType, area;

    /**
     * text preferences for specific values if the user knows them
     */
    private static EditTextPreference usage, exactArea;

    /**
     * boolean saying whether the user knows the fuel usage of his car
     */
    private static SwitchPreference knowsUsage;

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName);
    }


    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            usedTransportation = (MultiSelectListPreference) findPreference(getString(R.string.pref_key_used_transportation));
            carType = (ListPreference) findPreference(getString(R.string.pref_key_car_type));
            fuelType = (ListPreference) findPreference(getString(R.string.pref_key_fuel_type));
            knowsUsage = (SwitchPreference) findPreference(getString(R.string.pref_key_knows_usage));
            usage = (EditTextPreference) findPreference((getString(R.string.pref_key_usage)));
            SwitchPreference knowsArea = (SwitchPreference) findPreference(getString(R.string.pref_key_knows_area));
            exactArea = (EditTextPreference) findPreference((getString(R.string.pref_key_exact_area)));
            area = (ListPreference) findPreference(getString(R.string.pref_key_area));

            // set visibility of the settings according to the super settings on which they depend
            if (usedTransportation.getValues().contains(Integer.toString(TRANSPORTATION_CAR))) {
                carType.setEnabled(true);
                fuelType.setEnabled(true);
                knowsUsage.setEnabled(true);
                usage.setEnabled(true);
            } else {
                carType.setEnabled(false);
                fuelType.setEnabled(false);
                knowsUsage.setEnabled(false);
                usage.setEnabled(false);
            }

            if (knowsUsage.isChecked()){
                carType.setEnabled(false);
            } else {
                usage.setEnabled(false);
            }

            knowsUsage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d(TAG, "values: " + newValue);

                    if (Boolean.valueOf(newValue.toString())) {
                        carType.setEnabled(false);
                        usage.setEnabled(true);
                    } else {
                        carType.setEnabled(true);
                        usage.setEnabled(false);
                    }
                    return true;
                }
            });

            if (knowsArea.isChecked()){
                area.setEnabled(false);
            } else {
                exactArea.setEnabled(false);
            }

            knowsArea.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d(TAG, "values: " + newValue);

                    if (Boolean.valueOf(newValue.toString())) {
                        area.setEnabled(false);
                        exactArea.setEnabled(true);
                    } else {
                        area.setEnabled(true);
                        exactArea.setEnabled(false);
                    }
                    return true;
                }
            });

            usedTransportation.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d(TAG, "values: " + ((MultiSelectListPreference) preference).getValues().toString());

                    if (newValue.toString().contains(Integer.toString(TRANSPORTATION_CAR))) {
                        fuelType.setEnabled(true);
                        knowsUsage.setEnabled(true);
                        if (knowsUsage.isChecked()) {
                            usage.setEnabled(true);
                        } else {
                            carType.setEnabled(true);
                        }
                    } else {
                        carType.setEnabled(false);
                        fuelType.setEnabled(false);
                        knowsUsage.setEnabled(false);
                        usage.setEnabled(false);
                    }
                    return true;
                }
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(false);
        }
    }
}
