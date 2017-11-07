package ch.ethz.inf.se.a1.smartenergy.introfragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import ch.ethz.inf.se.a1.smartenergy.R;
import ch.ethz.inf.se.a1.smartenergy.SettingsActivity;

/**
 * fragment used in an intro slide telling the user that everything is set up
 */
public class AllSet extends Fragment implements ISlideBackgroundColorHolder {

    /**
     * tag for logging
     */
    private final static String TAG = "Lifestyle";

    /**
     * view holding the elements of this fragment
     */
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_set, container, false);

        TextView url = (TextView) view.findViewById(R.id.textViewUrl);
        url.setMovementMethod(LinkMovementMethod.getInstance());

        TextView email = (TextView) view.findViewById(R.id.textViewEmail);
        email.setMovementMethod(LinkMovementMethod.getInstance());

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