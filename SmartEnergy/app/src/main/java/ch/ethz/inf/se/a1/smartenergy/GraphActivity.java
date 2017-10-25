package ch.ethz.inf.se.a1.smartenergy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class GraphActivity extends AppCompatActivity {
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        mListView = (ListView) findViewById(R.id.stats_list_view);
        ArrayList<Day> dayList = new ArrayList<Day>();

        String[] listItems = new String[7];
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        for (int i = 0; i < 7; i++){
            today.add(Calendar.DATE, -1);
            ArrayList<Update> updates = UpdateService.loadDaysUpdates(today, this);
            Day currentDay = new Day(updates, this);
            dayList.add(currentDay);
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);

//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, -1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series);
//        series.setSpacing(20);

    }
}
