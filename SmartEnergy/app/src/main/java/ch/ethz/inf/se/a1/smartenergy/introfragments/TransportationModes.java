package ch.ethz.inf.se.a1.smartenergy.introfragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import java.util.HashSet;
import java.util.Set;

import ch.ethz.inf.se.a1.smartenergy.R;
import ch.ethz.inf.se.a1.smartenergy.SettingsActivity;

public class TransportationModes extends Fragment implements ISlideBackgroundColorHolder {

    private final static String TAG = "TransportationModes";

    View view;

    SharedPreferences pref;
    SharedPreferences.Editor ed;

    Set<String> selectionsCopy = new HashSet<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transportation_modes, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        ed = pref.edit();

        Set<String> selections = pref.getStringSet(getString(R.string.pref_key_used_transportation), null);
        if (selections != null) {
            selectionsCopy.addAll(selections);
            String[] selected = selections.toArray(new String[]{});
            Log.d(TAG, "selected are: " + selected.toString());

            for (String aSelected : selected) {
                switch (Integer.parseInt(aSelected)) {
                    case SettingsActivity.TRANSPORTATION_AIRPLANE:
                        ((CheckBox) view.findViewById(R.id.checkBoxAirplane)).toggle();
                        break;
                    case SettingsActivity.TRANSPORTATION_TRAIN:
                        ((CheckBox) view.findViewById(R.id.checkBoxTrain)).toggle();
                        break;
                    case SettingsActivity.TRANSPORTATION_CAR:
                        ((CheckBox) view.findViewById(R.id.checkBoxCar)).toggle();
                        break;
                    case SettingsActivity.TRANSPORTATION_TRAM:
                        ((CheckBox) view.findViewById(R.id.checkBoxTram)).toggle();
                        break;
                    case SettingsActivity.TRANSPORTATION_BICYCLE:
                        ((CheckBox) view.findViewById(R.id.checkBoxBicycle)).toggle();
                        break;
                }
            }
        } else {
            Log.d(TAG, "nothing selected");
        }

        ((CheckBox) view.findViewById(R.id.checkBoxAirplane)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            selectionsCopy.add(getString(R.string.one));
                        } else {
                            selectionsCopy.remove(getString(R.string.one));
                        }
                        ed.putStringSet(getString(R.string.pref_key_used_transportation), selectionsCopy);
                        ed.apply();
                    }
                }
        );

        ((CheckBox) view.findViewById(R.id.checkBoxTrain)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            selectionsCopy.add(getString(R.string.two));
                        } else {
                            selectionsCopy.remove(getString(R.string.two));
                        }
                        ed.putStringSet(getString(R.string.pref_key_used_transportation), selectionsCopy);
                        ed.apply();
                    }
                }
        );

        ((CheckBox) view.findViewById(R.id.checkBoxCar)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            selectionsCopy.add(getString(R.string.three));
                        } else {
                            selectionsCopy.remove(getString(R.string.three));
                        }
                        ed.putStringSet(getString(R.string.pref_key_used_transportation), selectionsCopy);
                        ed.apply();
                    }
                }
        );

        ((CheckBox) view.findViewById(R.id.checkBoxTram)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            selectionsCopy.add(getString(R.string.four));
                        } else {
                            selectionsCopy.remove(getString(R.string.four));
                        }
                        ed.putStringSet(getString(R.string.pref_key_used_transportation), selectionsCopy);
                        ed.apply();
                    }
                }
        );

        ((CheckBox) view.findViewById(R.id.checkBoxBicycle)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            selectionsCopy.add(getString(R.string.five));
                        } else {
                            selectionsCopy.remove(getString(R.string.five));
                        }
                        ed.putStringSet(getString(R.string.pref_key_used_transportation), selectionsCopy);
                        ed.apply();
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