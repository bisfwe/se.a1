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
import android.widget.EditText;
import android.widget.RadioButton;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import ch.ethz.inf.se.a1.smartenergy.R;
import ch.ethz.inf.se.a1.smartenergy.SettingsActivity;

public class Lifestyle extends Fragment implements ISlideBackgroundColorHolder {

    private final static String TAG = "Lifestyle";

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
        view = inflater.inflate(R.layout.lifestyle, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        ed = pref.edit();

        // get settings to display radio buttons
        String lifestyle = pref.getString(getString(R.string.pref_key_diet_type), Integer.toString(SettingsActivity.LIFESTYLE_AVERAGE));
        switch (Integer.parseInt(lifestyle)) {
            case SettingsActivity.LIFESTYLE_MEET_LOVER:
                ((RadioButton) view.findViewById(R.id.radio_meet_lover)).toggle();
                break;
            case SettingsActivity.LIFESTYLE_NO_BEEF:
                ((RadioButton) view.findViewById(R.id.radio_no_beef)).toggle();
                break;
            case SettingsActivity.LIFESTYLE_VEGETARIAN:
                ((RadioButton) view.findViewById(R.id.radio_vegetarian)).toggle();
                break;
            case SettingsActivity.LIFESTYLE_VEGAN:
                ((RadioButton) view.findViewById(R.id.radio_vegan)).toggle();
                break;
            case SettingsActivity.LIFESTYLE_AVERAGE:
            default:
                ((RadioButton) view.findViewById(R.id.radio_average)).toggle();
        }

        // add listeners to the radio buttons to set settings
        ((RadioButton) view.findViewById(R.id.radio_meet_lover)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_diet_type), getString(R.string.one));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_average)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_diet_type), getString(R.string.two));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_no_beef)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_diet_type), getString(R.string.three));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_vegetarian)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_diet_type), getString(R.string.four));
                            ed.apply();
                        }
                    }
                }
        );

        ((RadioButton) view.findViewById(R.id.radio_vegan)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ed.putString(getString(R.string.pref_key_diet_type), getString(R.string.five));
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