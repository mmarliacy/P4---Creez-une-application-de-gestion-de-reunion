package com.example.myreu.ui.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.myreu.R;
import com.example.myreu.ui.di.DI;
import com.example.myreu.ui.model.Meeting;
import com.example.myreu.ui.model.Room;
import com.example.myreu.ui.service.DummyReuApiService;
import java.util.Objects;

public class ReuInformation extends AppCompatActivity {

    //-- FOR DESIGN -->
    private TextView meetingSubject,meetingPlace,meetingTime,participants, meetingDate;
    private ImageView iconRoom;

    //-- IMPORT DATA & INSTANCE -->
    public Meeting myReu;
    public Room room;
    public DummyReuApiService myReuApiService;

    //-- ON-CREATE PROCESS -->
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myreu_informations);

        //-- Get an instance of Fake API to use methods --
        myReuApiService = DI.getListReuApiService();

        //-- Get intent & extra informations --
        myReu = getIntent().getParcelableExtra("item");
        room = getIntent().getParcelableExtra("icon");

        //-- Set ActionBar --
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //-- Set required informations
        getGraphicsElements();
        setInformation(room);
    }
    //-- Declare graphics attributes --
    private void getGraphicsElements() {
        meetingSubject = findViewById(R.id.myReu_subject);
        meetingPlace = findViewById(R.id.meeting_place);
        meetingDate =findViewById(R.id.meeting_date);
        meetingTime = findViewById(R.id.myReu_time);
        participants = findViewById(R.id.participants_mails);
        iconRoom = findViewById(R.id.icon_room);
    }
    //-- Set information get by parcelable method & Intent into graphics attributes --
    public void setInformation(Room room) {
        Glide.with(this).load(room.getIcon()).placeholder(R.drawable.ic_baseline_stars_24).into(iconRoom);
        meetingTime.setText(String.valueOf(myReu.getTime()));
        meetingSubject.setText(myReu.getSubject());
        meetingDate.setText(myReu.getDate());
        meetingPlace.setText(room.getRoom());
        participants.setText(myReu.getParticipant().toString().substring(1, myReu.getParticipant().toString().length() - 1));
    }
}

