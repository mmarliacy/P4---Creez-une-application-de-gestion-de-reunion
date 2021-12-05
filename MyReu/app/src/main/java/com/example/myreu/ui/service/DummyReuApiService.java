package com.example.myreu.ui.service;



import com.example.myreu.ui.model.Meeting;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DummyReuApiService implements MyReuApiService {

    private final ArrayList<Meeting> getMyReu = new ArrayList<>();

    /**
     * Get List of meetings
     * @return {@link ArrayList}
     */
    public ArrayList<Meeting> getAllMyReu() {
        return getMyReu;
    }

    /**
     * With this method we add a meeting to our list
     */
    public void addAReu(Meeting meeting) {
        getMyReu.add(meeting);
    }

    /**
     * With this method we delete a meeting from our list
     */
    public void deleteAReu(Meeting meeting) {
        getMyReu.remove(meeting);
    }

    /**
     * With this method we get dates filtered by comparing two dates between them
     * @return {@link ArrayList}
     */
    @Override
    public ArrayList<Meeting> getReuFilteredByDate(Date date) throws ParseException {

        ArrayList<Meeting> dateFilterSet = new ArrayList<>();

        //-- Get an instance of calendar and get date type by the user (Date-1) --
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);

        //-- Get a second instance of calendar and get date (Date -2) selected
        //   for the comparing to Date-1 by iteration method --
        for (int i = 0; i < getMyReu.size(); i++ ) {
            Calendar cal2 = Calendar.getInstance();
            String dateS = getMyReu.get(i).getDate();

        //-- Get String Date turned into Date date --
            @SuppressLint("SimpleDateFormat")
            Date dateD = new SimpleDateFormat("dd/MM/yy").parse(dateS);
            assert dateD != null;
            cal2.setTime(dateD);

        //-- Comparing --
            boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
                    && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
            if(sameDay) dateFilterSet.add(getMyReu.get(i));
        }
        //-- Return result - Date list filtered --
        return dateFilterSet;
    }
}
