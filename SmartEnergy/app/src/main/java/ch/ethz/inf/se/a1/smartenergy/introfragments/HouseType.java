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

/**
 * fragment used in an intro slide asking the user for his housing preferences
 */
public class HouseType extends Fragment implements ISlideBackgroundColorHolder {

    /**
     * tag for logging
     */
    private final static String TAG = "HouseType";

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_house_type, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        ed = pref.edit();

        // get settings to display radio buttons
        String carType = pref.getString(getString(R.string.pref_key_age), Integer.toString(SettingsActivity.HOME_RENOVATED));
        switch (Integer.parseInt(carType)) {
            case SettingsActivity.HOME_NEW:
                ((RadioButton) view.findViewById(R.id.radio_recent)).toggle();
                break;
            case SettingsActivity.HOME_OLD:
                ((RadioButton) view.findViewById(R.id.radio_old)).toggle();
                break;
            case SettingsActivity.HOME_MINERGIE:
                ((RadioButton) view.findViewById(R.id.radio_minergie)).toggle();
                break;
            case SettingsActivity.HOME_P:
                ((RadioButton) view.findViewById(R.id.radio_p)).toggle();
                break;
            case SettingsActivity.HOME_A:
                ((RadioButton) view.findViewById(R.id.radio_a)).toggle();
                break;
            case SettingsActivity.HOME_RENOVATED:
            default:
                ((RadioButton) view.findViewById(R.id.radio_renovated)).toggle();
        }

        // add listeners to the radio buttons to set settings
        ((RadioButton) view.findViewById(R.id.radio_recent)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_age), getString(R.string.one));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_renovated)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_age), getString(R.string.two));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_old)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_age), getString(R.string.three));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_minergie)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_age), getString(R.string.four));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_p)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_age), getString(R.string.five));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_a)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_age), getString(R.string.six));
                            ed.apply();
                        }
                    }
                }
        );

        return view;
    }

    @Override
    public int getDefaultBackgroundColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        if (view != null) {
            view.setBackgroundColor(backgroundColor);
        }
    }
}