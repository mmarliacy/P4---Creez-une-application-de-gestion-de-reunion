package com.example.myreu.ui.ui;

import android.annotation.SuppressLint;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import java.util.Objects;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.bumptech.glide.Glide;

import com.example.myreu.ui.di.DI;
import com.example.myreu.ui.model.Meeting;
import com.example.myreu.ui.model.Room;
import com.example.myreu.ui.service.DummyReuApiService;

import com.example.myreu.R;
import androidx.appcompat.app.AppCompatActivity;


public class AddReuActivity extends AppCompatActivity {

    //-- GRAPHICS ELEMENTS -->
    private TextInputLayout subjectInput,participantInput;
    private TextInputEditText dateInput, timeInput;
    private ImageButton addParticipantBtn;
    Button addReuBtn;
    ChipGroup chipGroup;
    String[] tags;

    //-- LISTS -->
    List<String> participants = new ArrayList<>();
    List<String> roomName = new ArrayList<>();
    List<Integer> iconRoom = new ArrayList<>();
    List<Room> roomList = new ArrayList<>();

    //-- DATE-TIME-PICKER VARIABLES -->
    int hour, minute;

    //-- ROOM VARIABLES -->
    String chosenRoom;
    Integer icon;
    Room selectedRoom = new Room(chosenRoom, icon);

    //-- IMPORTED CLASSES -->
    Meeting myReu;
    public DummyReuApiService myReuApiService;

    //-- ON-CREATE PROCESS -->
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reu);

        //-- Get an instance of Fake API to use methods --
        myReuApiService = DI.getListReuApiService();

        //-- Get graphics attributes --
        getGraphicsElements();

        //-- Set today time & date, Init Time & Date Picker --
        timeInput.setText(getTodayTime());
        dateInput.setText(getTodayDate());
        initTimePicker();
        initDatePicker();

        //-- Init & set Rooms --
        getRooms();
        setRooms();

        //-- Set chipGroup --
        connectChip();

        //-- Return to MyReuActivity --
        addReuIntent();
    }
    //-- Declare graphics attributes --
    private void getGraphicsElements() {
        addReuBtn = findViewById(R.id.addReu_Btn);
        subjectInput = findViewById(R.id.add_subject_txf);
        timeInput = findViewById(R.id.add_time_edit);
        participantInput = findViewById(R.id.add_participants_txf);
        chipGroup = findViewById(R.id.chip_group_add_bloc);
        addParticipantBtn = findViewById(R.id.add_participants_btn);
        dateInput = findViewById(R.id.add_date_edit);
    }
    //-- DATE & TIME CONFIGURATIONS -->

    //-- Display current time when AddReuActivity open --
    private String getTodayTime() {
        Calendar cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR);
        minute = cal.get(Calendar.MINUTE);
        return timeString(hour, minute);
    }
    private String timeString(int hour, int minute) {
        return hour + " : " + minute;
    }

    //-- Display current date when AddReuActivity open --
    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        month += 1;
        return dateString(year, month, day);
    }
    private String dateString(int year, int month, int day) {
        return day+"/"+month+"/"+year;
    }

    //-- Init & get Time Picker Dialog Value --
    private void initTimePicker() {
        timeInput.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    AddReuActivity.this,
                    (timePicker, hourOfDay, minutes) -> {
                        hour = hourOfDay;
                        minute = minutes;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,hour, minute);
                        timeInput.setText(DateFormat.format("hh:mm aa", calendar));
                    }, 12,0, true
            );
            timePickerDialog.updateTime(hour, minute);
            timePickerDialog.show();
        });
    }

    //-- Init & get Time Picker Dialog Value --
    private void initDatePicker() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        dateInput.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddReuActivity.this, (datePicker, year1, month1, day1) -> {
                month1 += 1;
                String date = day1 +"/"+ month1 +"/"+ year1;
                dateInput.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });
    }
    //-- HERE  IS THE LIST OF ROOMS (CLASS) -->
    public void getRooms() {
        //-- Creation of rooms' names --
        roomName.add("Peach");
        roomName.add("Mario");
        roomName.add("Luigi");
        roomName.add("Toad");
        roomName.add("Yoshi");
        roomName.add("Wario");
        roomName.add("Waluigi");
        roomName.add("Daisy");

        //-- Creation of rooms' icons --
        iconRoom.add(R.drawable.peach_circle);
        iconRoom.add(R.drawable.mario_circle);
        iconRoom.add(R.drawable.luigi_circle);
        iconRoom.add(R.drawable.toad_circle);
        iconRoom.add(R.drawable.yoshi_circle);
        iconRoom.add(R.drawable.wario_circle);
        iconRoom.add(R.drawable.waluigi_circle);
        iconRoom.add(R.drawable.daisy_circle);
    }

    //-- DROP DOWN LIST ROOMS CONFIGURATIONS -->
    public void setRooms() {
        AutoCompleteTextView atvRoomInput = findViewById(R.id.act_add_place);

        //-- Find nameRoom position in Drop down list --
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, roomName);
        atvRoomInput.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        atvRoomInput.setOnItemClickListener((adapterView, view, position, l) -> {

            String chosenRoom = arrayAdapter.getItem(position);
            selectedRoom.setRoom(chosenRoom);

            //For each name, an icon at same position is associate to it
            for (String rooms : roomName) {
                int count = arrayAdapter.getPosition(chosenRoom);
                icon = iconRoom.get(count);
                selectedRoom.setIcon(icon);
                roomList.add(new Room(rooms, icon));
                arrayAdapter.notifyDataSetChanged();
            }
            ImageView iconRoom = findViewById(R.id.icon_room);
            Glide.with(this).load(icon).placeholder(R.drawable.ic_baseline_stars_24).into(iconRoom);
        });

    }
    //-- PARTICIPANTS LIST & CHIP CONFIGURATIONS -->
    public void connectChip() {
        //-- On-Click on "add participants button" the tag is added as chip --
        addParticipantBtn.setOnClickListener(view -> {
            tags = Objects.requireNonNull(participantInput.getEditText()).getText().toString().split(" ");
            LayoutInflater layoutInflater = LayoutInflater.from(AddReuActivity.this);

            //-- For each new tag associate Chip to a text --
            for (String text : tags) {
                @SuppressLint("InflateParams")
                Chip chip = (Chip) layoutInflater.inflate(R.layout.chip_item, null, true);
                chip.setText(text);

                //-- On-click on closeIcon remove Chip --
                chip.setOnCloseIconClickListener(view1 -> {
                    chipGroup.removeView(chip);
                    participants.remove(text);
                });
                //-- On-click add new Chip --
                chipGroup.addView(chip);

                //-- For each tag added as text, add new element to participants list --
                participants.add(text);
            }
            //-- Clear TextInput after adding chip to participants list --
            Objects.requireNonNull(participantInput.getEditText()).getText().clear();
        });
    }

    //--> MEETING CREATION PROCESS -->

    //-- On-click on AddReuBtn we check answers --
    private void addReuIntent() {
        addReuBtn.setOnClickListener(view -> {
                checkAnswers();

        });
    }
    //-- Check if form has been completed --
    private void checkAnswers() {
        TextInputLayout roomInput = findViewById(R.id.add_place_txf);

        String subjectTextInput = Objects.requireNonNull(subjectInput.getEditText())
                .getText().toString();
        String chosenRoomInput = Objects.requireNonNull(roomInput.getEditText())
                .getText().toString();

        if(subjectTextInput.isEmpty()) {
            subjectInput.setError("Renseignez un sujet");
            return;
        }
        if(chosenRoomInput.isEmpty()) {
            roomInput.setError("Choisissez une salle");
            return;
        }
        if(participants.isEmpty()) {
            participantInput.setError("Renseignez au moins un participant");
            return;
        }
        //-- If form has been completed we create a new meeting with extras informations --
        addReu();
        Intent intent = new Intent(getApplicationContext(), MyReuActivity.class);
        intent.putExtra("chosenRoom", chosenRoom);
        startActivity(intent);
    }
    //-- Add a new meeting based on meeting variables --
    public void addReu() {
        myReu = new Meeting(
            Objects.requireNonNull(subjectInput.getEditText()).getText().toString(),
            selectedRoom,
            dateInput.getEditableText().toString(),
            timeInput.getEditableText().toString(),
            participants
        );
    myReuApiService.addAReu(myReu);
    finish();
    }

    //-- EVENT-BUS PROCESSING -->
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
}