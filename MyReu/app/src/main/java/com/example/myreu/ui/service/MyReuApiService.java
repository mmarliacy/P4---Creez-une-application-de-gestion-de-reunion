package com.example.myreu.ui.service;

import com.example.myreu.ui.model.Meeting;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface MyReuApiService {

    /**
     * Get all Meetings
     * @return {@link List}
     */
    List<Meeting> getAllMyReu();

    /**
     * Add a Meeting
     */
    void addAReu(Meeting meeting);

    /**
     * Delete a Meeting
     */
    void deleteAReu(Meeting meeting);

    /**
     * With this method we get dates filtered by comparing two dates between them
     * @return {@link ArrayList}
     */
    ArrayList<Meeting> getReuFilteredByDate(Date date) throws ParseException;
}
