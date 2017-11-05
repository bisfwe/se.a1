package ch.ethz.inf.se.a1.smartenergy.introfragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import ch.ethz.inf.se.a1.smartenergy.R;
import ch.ethz.inf.se.a1.smartenergy.SettingsActivity;

/**
 * fragment used in an intro slide asking the user for car usage preferences
 */
public class CarUsage extends Fragment implements ISlideBackgroundColorHolder {

    /**
     * tag for logging
     */
    private final static String TAG = "CarUsage";

    /**
     * view holding the elements of this fragment
     */
    View view;

    /**
     * shared preferences
     */
    SharedPreferences pref;

    /**
     * editor for shared preferences
     */
    SharedPreferences.Editor ed;

    /**
     * text field to specify the fuel usage
     */
    EditText fuelUsage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_car_usage, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        ed = pref.edit();

        // get fuel consumption value and set it
        String fuelConsumption = pref.getString(getString(R.string.pref_key_usage), "8");
        fuelUsage = (EditText) view.findViewById(R.id.editTextFuelUsage);
        fuelUsage.setText(fuelConsumption);

        // write value to settings if changed
        fuelUsage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ed.putString(getString(R.string.pref_key_usage), s.toString());
                ed.putBoolean(getString(R.string.pref_key_knows_usage), true);
                ed.apply();
            }
        });

        // get settings to display radio buttons
        String carType = pref.getString(getString(R.string.pref_key_car_type), Integer.toString(SettingsActivity.TRANSPORTATION_MEDIUM_CAR));
        switch (Integer.parseInt(carType)) {
            case SettingsActivity.TRANSPORTATION_SMALL_CAR:
                ((RadioButton) view.findViewById(R.id.radio_small)).toggle();
                break;
            case SettingsActivity.TRANSPORTATION_BIG_CAR:
                ((RadioButton) view.findViewById(R.id.radio_big)).toggle();
                break;
            case SettingsActivity.TRANSPORTATION_MEDIUM_CAR:
            default:
                ((RadioButton) view.findViewById(R.id.radio_medium)).toggle();
        }

        // add listeners to the radio buttons to set settings
        ((RadioButton) view.findViewById(R.id.radio_small)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            fuelUsage.setText("5");
                            ed.putString(getString(R.string.pref_key_usage), "5");
                            ed.putString(getString(R.string.pref_key_car_type), getString(R.string.one));
                            ed.putBoolean(getString(R.string.pref_key_knows_usage), false);
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_medium)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            fuelUsage.setText("8");
                            ed.putString(getString(R.string.pref_key_usage), "8");
                            ed.putString(getString(R.string.pref_key_car_type), getString(R.string.two));
                            ed.putBoolean(getString(R.string.pref_key_knows_usage), false);
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_big)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            fuelUsage.setText("12");
                            ed.putString(getString(R.string.pref_key_usage), "12");
                            ed.putString(getString(R.string.pref_key_car_type), getString(R.string.three));
                            ed.putBoolean(getString(R.string.pref_key_knows_usage), false);
                            ed.apply();
                        }
                    }
                }
        );

        // get settings to display fuel type radio buttons
        String fuelType = pref.getString(getString(R.string.pref_key_fuel_type), Integer.toString(SettingsActivity.TRANSPORTATION_PETROL));
        switch (Integer.parseInt(fuelType)) {
            case SettingsActivity.TRANSPORTATION_DIESEL:
                ((RadioButton) view.findViewById(R.id.radio_diesel)).toggle();
                break;
            case SettingsActivity.TRANSPORTATION_PETROL:
            default:
                ((RadioButton) view.findViewById(R.id.radio_petrol)).toggle();
        }

        // add listeners to the radio buttons to set settings
        ((RadioButton) view.findViewById(R.id.radio_petrol)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_fuel_type), getString(R.string.one));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_diesel)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_fuel_type), getString(R.string.two));
                            ed.apply();
                        }
                    }
                }
        );

        return view;
    }

    @Override
    public int getDefaultBackgroundColor() {
        return getResources().getColor(R.color.orange);
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        if (view != null) {
            view.setBackgroundColor(backgroundColor);
        }
    }
}