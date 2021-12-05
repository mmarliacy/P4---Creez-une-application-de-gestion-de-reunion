package com.example.myreu.ui.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myreu.R;
import com.example.myreu.ui.event.DeleteReuEvent;
import com.example.myreu.ui.model.Meeting;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;

public class MyReuRecyclerViewAdapter extends RecyclerView.Adapter<MyReuRecyclerViewAdapter.ViewHolder> implements Filterable {

    private final ArrayList<Meeting> meetingList;
    private final ArrayList<Meeting> meetingListFull;

    //-- ADAPTER CONSTRUCTOR  -->
    public MyReuRecyclerViewAdapter(ArrayList<Meeting> items) {
        this.meetingList = items;
        meetingListFull = new ArrayList<>(meetingList);
    }

    //-- RECYCLERVIEW CONFIGURATION -->
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myreu_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Meeting item = meetingList.get(position);
        holder.setInformation(item);
        Glide.with(holder.icon.getContext())
                .load(item.getRoom().getIcon()).into(holder.icon);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ReuInformation.class);
            intent.putExtra("item", item);
            intent.putExtra("icon", item.getRoom());
            view.getContext().startActivity(intent);
        });
        //-- PUBLISH DELETE-REU-EVENT -->
        holder.deleteBtn.setOnClickListener(view ->
                EventBus.getDefault().post(new DeleteReuEvent(item)));
    }

    @Override
    public int getItemCount() {
        return meetingList.size();
    }

    //-- VIEW HOLDER CLASS CONFIGURATION -->
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //-- Graphics Attributes for design --
        public TextView subject;
        public TextView time;
        public TextView room;
        public TextView participants;
        public ImageView icon;
        public ImageButton deleteBtn;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);

            //-- Declare graphics attributes --
            subject = itemView.findViewById(R.id.item_meeting_subject);
            time = itemView.findViewById(R.id.item_meeting_time);
            room = itemView.findViewById(R.id.item_meeting_room);
            participants = itemView.findViewById(R.id.item_meeting_participants);
            date = itemView.findViewById(R.id.item_date);
            icon = itemView.findViewById(R.id.icon_room);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
        //-- Set information get by parcelable method into graphics attributes --
        @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
        public void setInformation(Meeting myReu) {
            subject.setText(myReu.getSubject());
            time.setText(myReu.getTime());
            room.setText(myReu.getRoom().getRoom());
            date.setText(String.valueOf(myReu.getDate()));
            participants.setText(myReu.getParticipant().toString()
                    .substring(1, myReu.getParticipant().toString().length() - 1));
        }
    }
    //-- CREATE FILTER METHOD BASED ON RECYCLERVIEW -->
    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {

        //-- Performing filter based on names of Room in RV by focusing on chars of each elements --
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Meeting> filteredMeetingList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredMeetingList.addAll(meetingListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Meeting item : meetingListFull) {
                    if (item.getRoom().getRoom().toLowerCase().contains(filterPattern)) {
                        filteredMeetingList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredMeetingList;
            return filterResults;
        }
        //-- Clean, publish elements in recyclerview and notify to the adapter --
        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                meetingList.clear();
                meetingList.addAll(((List)filterResults.values));
                notifyDataSetChanged();
        }
    };
}