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

import java.util.Set;

import ch.ethz.inf.se.a1.smartenergy.R;

public class TransportationModes extends Fragment {

    private final static String TAG = "TransportationModes";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.transportation_modes, container, false);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Set<String> selections = sharedPrefs.getStringSet(getString(R.string.pref_key_used_transportation), null);
        if (selections != null) {
            String[] selected = selections.toArray(new String[]{});
            Log.d(TAG, "selected are: " + selected.toString());

            for (String aSelected : selected) {
                switch (Integer.parseInt(aSelected)) {
                    case 1:
                        ((CheckBox) getView().findViewById(R.id.checkBoxAirplane)).toggle();
                        break;
                    case 2:
                        ((CheckBox) view.findViewById(R.id.checkBoxAirplane)).toggle();
                        break;
                        // TODO: Raphael: das fertig mache
                }
            }
        } else {
            Log.d(TAG, "nothing selected");
        }

        return view;
    }
}