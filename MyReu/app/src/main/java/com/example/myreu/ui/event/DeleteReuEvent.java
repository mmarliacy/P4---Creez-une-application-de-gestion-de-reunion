package com.example.myreu.ui.event;

import com.example.myreu.ui.model.Meeting;

public class DeleteReuEvent {

    /**
     * Reu to delete
     */
    public Meeting myReu;

    /**
     * Constructor.
     */
    public DeleteReuEvent(Meeting myReu) {
        this.myReu = myReu;

    }
}
