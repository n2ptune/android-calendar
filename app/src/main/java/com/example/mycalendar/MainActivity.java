package com.example.mycalendar;

import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    MaterialCalendarView mcv;
    private final onDateDecorator oneDayDecorator = new onDateDecorator();
    Cursor Cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mcv = findViewById(R.id.calendarView);

        mcv.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator
        );

        String[] result = {"2019,5,30", "2019,7,1", "2019,5,25"};

        System.out.println(result);

        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            for(int i = 0 ; i < Time_Result.length ; i++){
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                calendar.set(year,month-1,dayy);
                dates.add(day);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            mcv.addDecorator(new EventDecorator(Color.RED, calendarDays,MainActivity.this));
        }
    }
}