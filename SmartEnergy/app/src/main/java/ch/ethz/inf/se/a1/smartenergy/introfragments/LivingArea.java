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

public class LivingArea extends Fragment implements ISlideBackgroundColorHolder {

    private final static String TAG = "LivingArea";

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
        view = inflater.inflate(R.layout.living_area, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        ed = pref.edit();

        final EditText editText = (EditText) view.findViewById(R.id.editTextAreaUsage);

        // get fuel consumption value and set it
        String areaUsage = pref.getString(getString(R.string.pref_key_area), "80");
        editText.setText(areaUsage);

        // write value to settings if changed
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ed.putString(getString(R.string.pref_key_area), s.toString());
                ed.putBoolean(getString(R.string.pref_key_knows_area), true);
                ed.apply();
            }
        });

        // get settings to display radio buttons
        String carType = pref.getString(getString(R.string.pref_key_area), Integer.toString(SettingsActivity.HOME_FLAT));
        switch (Integer.parseInt(carType)) {
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
                            editText.setText("16");
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
                            editText.setText("80");
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
                            editText.setText("180");
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
                            editText.setText("500");
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