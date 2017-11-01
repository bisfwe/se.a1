package ch.ethz.inf.se.a1.smartenergy.introfragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import ch.ethz.inf.se.a1.smartenergy.R;
import ch.ethz.inf.se.a1.smartenergy.SettingsActivity;

public class HeatingType extends Fragment implements ISlideBackgroundColorHolder {

    private final static String TAG = "HeatingType";

    View view;

    SharedPreferences pref;
    SharedPreferences.Editor ed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.heating_type, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        ed = pref.edit();

        // get settings to display radio buttons
        String carType = pref.getString(getString(R.string.pref_key_heating_type), Integer.toString(SettingsActivity.HOME_RENOVATED));
        switch (Integer.parseInt(carType)) {
            case SettingsActivity.HOME_GAS:
                ((RadioButton) view.findViewById(R.id.radio_gas)).toggle();
                break;
            case SettingsActivity.HOME_ELECTRIC:
                ((RadioButton) view.findViewById(R.id.radio_electric)).toggle();
                break;
            case SettingsActivity.HOME_AIR:
                ((RadioButton) view.findViewById(R.id.radio_air)).toggle();
                break;
            case SettingsActivity.HOME_GROUND:
                ((RadioButton) view.findViewById(R.id.radio_ground)).toggle();
                break;
            case SettingsActivity.HOME_OIL:
            default:
                ((RadioButton) view.findViewById(R.id.radio_oil)).toggle();
        }

        // add listeners to the radio buttons to set settings
        ((RadioButton) view.findViewById(R.id.radio_oil)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_heating_type), getString(R.string.one));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_gas)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_heating_type), getString(R.string.two));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_electric)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_heating_type), getString(R.string.three));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_air)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_heating_type), getString(R.string.four));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_ground)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_heating_type), getString(R.string.five));
                            ed.apply();
                        }
                    }
                }
        );

        return view;
    }

    @Override
    public int getDefaultBackgroundColor() {
        return getResources().getColor(R.color.turkey);
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        if (view != null) {
            view.setBackgroundColor(backgroundColor);
        }
    }
}