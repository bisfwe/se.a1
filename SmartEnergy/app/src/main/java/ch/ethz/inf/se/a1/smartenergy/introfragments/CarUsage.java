package ch.ethz.inf.se.a1.smartenergy.introfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import java.util.HashSet;
import java.util.Set;

import ch.ethz.inf.se.a1.smartenergy.R;

public class CarUsage extends Fragment implements ISlideBackgroundColorHolder {

    private final static String TAG = "CarUsage";

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
        view = inflater.inflate(R.layout.car_usage, container, false);
//        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
//        ed = pref.edit();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        EditText editText = (EditText) view.findViewById(R.id.editTextFuelUsage);
        editText.setText(sharedPrefs.getString(getString(R.string.pref_key_usage), "0.0"));
//        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

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