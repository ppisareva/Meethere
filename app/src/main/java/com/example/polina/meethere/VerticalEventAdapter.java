package com.example.polina.meethere;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polina.meethere.Adapters.Event;
import com.example.polina.meethere.Adapters.Events;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by polina on 09.03.16.
 */
public class VerticalEventAdapter extends RecyclerView.Adapter<VerticalEventAdapter.MyViewHolder> {

    List<Events> eventsList;
    Activity context;

    public VerticalEventAdapter(List<Events> eventsList, Activity context) {
        this.eventsList = eventsList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iner_list, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Events events = eventsList.get(position);
        holder.textView.setText(events.getCategory());
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        System.err.println("EVENT LIST: " + events.getEventList());
        HorizontalEventAdapter horizontalEventAdapter
                = new HorizontalEventAdapter(context, events.getEventList());
        holder.recyclerView.setAdapter(horizontalEventAdapter);
        holder.recyclerView.setNestedScrollingEnabled(false);
        holder.recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public int getItemCount() {
        return  (null!=eventsList? eventsList.size(): 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        Button button;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.list_learn_more);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.horizontal_list);
            textView = (TextView) itemView.findViewById(R.id.list_category);
        }
    }
}
