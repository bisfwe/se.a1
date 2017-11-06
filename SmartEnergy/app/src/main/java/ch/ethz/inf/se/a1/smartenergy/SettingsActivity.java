package ch.ethz.inf.se.a1.smartenergy;


import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        addPreferencesFromResource(R.xml.pref_general);

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

        if (knowsUsage.isChecked()) {
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

        if (knowsArea.isChecked()) {
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

        // set listener to ListPreference to set exact area if changed
        area.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int index = area.findIndexOfValue(newValue.toString());

                switch (index + 1) {
                    case HOME_ROOM:
                        exactArea.setText("16");
                        break;
                    case HOME_HOUSE:
                        exactArea.setText("180");
                        break;
                    case HOME_VILLA:
                        exactArea.setText("500");
                        break;
                    case HOME_FLAT:
                    default:
                        exactArea.setText("80");
                }
                return true;
            }
        });

        // set listener to ListPreference to set exact fuel usage if changed
        carType.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int index = area.findIndexOfValue(newValue.toString());

                switch (index + 1) {
                    case TRANSPORTATION_SMALL_CAR:
                        usage.setText("5");
                        break;
                    case TRANSPORTATION_BIG_CAR:
                        usage.setText("12");
                        break;
                    case TRANSPORTATION_MEDIUM_CAR:
                    default:
                        usage.setText("8");
                }
                return true;
            }
        });
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
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
