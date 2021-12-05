package com.example.myreu.ui.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;


public class Meeting implements Parcelable {

    private String subject;
    private Room room;
    private String time;
    private String date;
    private List<String> participant;


    public Meeting(String subject, Room room,String date, String time, List<String> participant) {
        this.subject = subject;
        this.room = new Room(room.getRoom(), room.getIcon());
        this.time = time;
        this.participant = participant;
        this.date = date;
    }

    /**
    * Meeting class implement Parcelable
     */
    protected Meeting(Parcel in) {
        subject = in.readString();
        date = in.readString();
        time = in.readString();
        participant = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeStringList(participant);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Meeting> CREATOR = new Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };

    // GETTERS AND SETTERS -->

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public List<String> getParticipant() {
        return participant;
    }

    public void setParticipant(List<String> participant) {
        this.participant = participant;
    }

}

