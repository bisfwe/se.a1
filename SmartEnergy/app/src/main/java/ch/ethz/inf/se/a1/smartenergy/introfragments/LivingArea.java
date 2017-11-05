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
 * fragment used in an intro slide asking the user for area usage preferences
 */
public class LivingArea extends Fragment implements ISlideBackgroundColorHolder {

    /**
     * tag for logging
     */
    private final static String TAG = "LivingArea";

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
     * text field to specify the area usage
     */
    EditText exactAreaUsage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_living_area, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        ed = pref.edit();

        exactAreaUsage = (EditText) view.findViewById(R.id.editTextAreaUsage);

        // get area value and set it
        String areaUsage = pref.getString(getString(R.string.pref_key_exact_area), "80");
        exactAreaUsage.setText(areaUsage);

        // write value to settings if changed
        exactAreaUsage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ed.putString(getString(R.string.pref_key_exact_area), s.toString());
                ed.putBoolean(getString(R.string.pref_key_knows_area), true);
                ed.apply();
            }
        });

        // get settings to display radio buttons
        String areaType = pref.getString(getString(R.string.pref_key_area), Integer.toString(SettingsActivity.HOME_FLAT));
        switch (Integer.parseInt(areaType)) {
            case SettingsActivity.HOME_ROOM:
                ((RadioButton) view.findViewById(R.id.radio_room)).toggle();
                break;
            case SettingsActivity.HOME_HOUSE:
                ((RadioButton) view.findViewById(R.id.radio_house)).toggle();
                break;
            case SettingsActivity.HOME_VILLA:
                ((RadioButton) view.findViewById(R.id.radio_villa)).toggle();
                break;
            case SettingsActivity.HOME_FLAT:
            default:
                ((RadioButton) view.findViewById(R.id.radio_flat)).toggle();
        }

        // add listeners to the radio buttons to set settings
        ((RadioButton) view.findViewById(R.id.radio_room)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            exactAreaUsage.setText("16");
                            ed.putString(getString(R.string.pref_key_exact_area), "16");
                            ed.putString(getString(R.string.pref_key_area), getString(R.string.one));
                            ed.putBoolean(getString(R.string.pref_key_knows_area), false);
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_flat)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            exactAreaUsage.setText("80");
                            ed.putString(getString(R.string.pref_key_exact_area), "80");
                            ed.putString(getString(R.string.pref_key_area), getString(R.string.two));
                            ed.putBoolean(getString(R.string.pref_key_knows_area), false);
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_house)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            exactAreaUsage.setText("180");
                            ed.putString(getString(R.string.pref_key_exact_area), "180");
                            ed.putString(getString(R.string.pref_key_area), getString(R.string.three));
                            ed.putBoolean(getString(R.string.pref_key_knows_area), false);
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_villa)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            exactAreaUsage.setText("500");
                            ed.putString(getString(R.string.pref_key_exact_area), "500");
                            ed.putString(getString(R.string.pref_key_area), getString(R.string.four));
                            ed.putBoolean(getString(R.string.pref_key_knows_area), false);
                            ed.apply();
                        }
                    }
                }
        );

        return view;
    }

    @Override
    public int getDefaultBackgroundColor() {
        return getResources().getColor(R.color.violet);
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        if (view != null) {
            view.setBackgroundColor(backgroundColor);
        }
    }
}