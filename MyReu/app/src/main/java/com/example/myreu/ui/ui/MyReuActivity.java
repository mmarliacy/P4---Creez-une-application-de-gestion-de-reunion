package com.example.myreu.ui.ui;

import androidx.annotation.NonNull;
import org.greenrobot.eventbus.Subscribe;
import android.annotation.SuppressLint;

import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.app.DatePickerDialog;
import java.util.Calendar;
import java.text.ParseException;

import com.example.myreu.R;
import android.widget.SearchView;
import org.greenrobot.eventbus.EventBus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;
import com.example.myreu.ui.di.DI;
import com.example.myreu.ui.event.DeleteReuEvent;
import com.example.myreu.ui.model.Meeting;
import com.example.myreu.ui.service.MyReuApiService;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MyReuActivity extends AppCompatActivity{

    //-- Get an instance of Fake API to use methods --
    public MyReuApiService myReuApiService= DI.getListReuApiService();

    //-- RECYCLERVIEW, ARRAYLIST, BUTTONS CONFIGURATION -->
    public ArrayList<Meeting> meetingList = new ArrayList<>(myReuApiService.getAllMyReu());
    public MyReuRecyclerViewAdapter adapter = new MyReuRecyclerViewAdapter(meetingList);
    public RecyclerView recyclerView;

    private FloatingActionButton addBtn;
    private SearchView searchView;

    @Override
    protected void attachBaseContext(Context Base) {
        super.attachBaseContext(Base);
        MultiDex.install(this);
    }

    //-- ON-CREATE PROCESS -->
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myreu_recyclerview_activity);

        getGraphicsElements();
        initRecyclerView();
        createReu();
    }
    //-- Declare graphics attributes --
    private void getGraphicsElements() {
        addBtn = findViewById(R.id.createReu_Btn);
        recyclerView = findViewById(R.id.recyclerView);
    }
    //-- Initialize Recyclerview --
    public void initRecyclerView() {
        setData();
        recyclerView.addItemDecoration(new DividerItemDecoration(MyReuActivity.this,
                DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    //-- Set Data for adapter --
    private void setData(){
        recyclerView.setAdapter(adapter);
    }
    //-- On-click create a meeting --
    private void createReu() {
        addBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MyReuActivity.this, AddReuActivity.class);
            startActivity(intent);
        });
    }

    //-- MENU SEARCH : ON-CREATE OPTIONS PROCESS -->
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        //-- Declare graphic menu attribute --
        MenuItem searchItem = menu.findItem(R.id.filter_by_place);

        //-- Set search view process  --
        searchView = (SearchView) searchItem.getActionView();
        setSearchViewListener();
        return true;
    }
    //-- Set search view by letter based on Filter declared in recyclerview adapter  --
    public void setSearchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }
    //-- MENU : ON-CLICK ON OPTIONS ITEM SELECTED -->
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_by_date:
                filterByDate();
                return true;

            case R.id.display_all:
                resetReu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //-- FILTER BY DATE OPTIONS -->
    private void filterByDate() {
        //-- Set today date when clicking on the item --
       Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

    //-- On-click apply filter created in MyReuApiService & get list of corresponding elements --
        @SuppressLint("NotifyDataSetChanged")
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year1, month1, day1)
                -> { cal.set(year1, month1, day1);
                     meetingList.clear();
            try {
                meetingList.addAll(myReuApiService.getReuFilteredByDate(cal.getTime()));
                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        };
        //-- Open DatePickerDialog --
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener,
                year, month, day);
        datePickerDialog.show();
    }
    //-- FILTER RESET -->
    @SuppressLint("NotifyDataSetChanged")
    private void resetReu() {
        meetingList.clear();
        meetingList.addAll(myReuApiService.getAllMyReu());
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
    }

    //-- EVENT-BUS PROCESSING -->
    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    //-- SUBSCRIBE TO DELETE-REU-EVENT -->
    @SuppressLint("NotifyDataSetChanged")
    @Subscribe
    public void onDeleteReu (DeleteReuEvent event) {
            myReuApiService.deleteAReu(event.myReu);
            meetingList.remove(event.myReu);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        }
}