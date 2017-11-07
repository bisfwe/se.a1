package ch.ethz.inf.se.a1.smartenergy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

/**
 * Created by simon on 17.10.17.
 */

public class DayAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Day> mDataSource;

    public DayAdapter(Context context, ArrayList<Day> days){
        mContext = context;
        mDataSource = days;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Day getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // populates the list view with content
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.stats_item_day, parent, false);

        // Get title element
        TextView titleTextView = (TextView) rowView.findViewById(R.id.day_list_title);

        // Get subtitle element
        TextView subtitleTextView = (TextView) rowView.findViewById(R.id.day_list_subtitle);
        TextView subEnergyTextView = (TextView) rowView.findViewById(R.id.day_list_subEnergy);

        Day day = getItem(position);
        DateTimeZone timeZone = DateTimeZone.forID( "Europe/Paris" );
        DateTime dateTime = new DateTime( day.date, timeZone );
        int dayOfWeekNumber = dateTime.getDayOfWeek(); // ISO 8601 standard says Monday is 1.
        DateTimeFormatter formatter = DateTimeFormat.forPattern( "EEEE" ).withLocale( java.util.Locale.ENGLISH );
        String dayOfWeekName = formatter.print( dateTime );

        titleTextView.setText(dayOfWeekName);
        subtitleTextView.setText("Carbon footprint: " + Double.valueOf(day.getTotalCo2()).intValue() + " kg");
        subEnergyTextView.setText("Energy used: " + Double.valueOf(day.getTotalEnergy()).intValue() + " kW/h");

        GraphView graph = (GraphView) rowView.findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, day.getTotalGreenCo2()),
                new DataPoint(1, day.getTotalYellowCo2()),
                new DataPoint(2, day.getTotalRedCo2()),
        });
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                int x = (int) data.getX();
                switch (x){
                    case 0:
                        return Color.rgb(59, 175, 71);
                    case 1:
                        return Color.rgb(249, 237, 4);
                    case 2:
                        return Color.rgb(255, 0, 55);
                    default: return Color.rgb(96, 2, 204);
                }
            }
        });
        series.setSpacing(10);
        graph.addSeries(series);
        GridLabelRenderer grid = graph.getGridLabelRenderer();
        grid.setHorizontalLabelsVisible(false);
        grid.setVerticalLabelsVisible(false);
        grid.setHighlightZeroLines(false);
        graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.NONE );

        return rowView;
    }



}
