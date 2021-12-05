package com.example.myreu.ui.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Room implements Parcelable {

    private String room;
    private Integer icon;

    public Room(String room, Integer icon){
        this.room = room;
        this.icon = icon;
    }

    /**
     * Room class implement Parcelable
     */
    protected Room(Parcel in) {
        room = in.readString();
        if (in.readByte() == 0) {
            icon = null;
        } else {
            icon = in.readInt();
        }
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    // GETTERS AND SETTERS -->

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getIcon() {
        return icon;
    }

    public Room setIcon(Integer icon) {
        this.icon = icon;
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(room);
        if (icon == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(icon);
        }
    }
}
