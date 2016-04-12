package com.example.polina.meethere;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polina.meethere.Adapters.Event;
import com.example.polina.meethere.Adapters.Events;

import java.util.List;

/**
 * Created by polina on 18.03.16.
 */
public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.ViewHolder> {

    List<Event> eventList;
    Context context;

    public MyEventsAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public MyEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_event, parent, false);
        MyEventsAdapter.ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.name.setText(event.getName());
        holder.image.setImageResource(event.getPhoto());
        holder.time.setText("24 Марта, 12:30");
        holder.rating.setText(event.getRating());
        holder.budget.setText(event.getBudget());

    }


    @Override
    public int getItemCount() {
        return (null != eventList ? eventList.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private ImageView image;
        private CheckBox imageJoin;
        private TextView name;
        private TextView rating;
        private TextView myEvent;
        private TextView budget;
        private TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image_my_event);
            name = (TextView) itemView.findViewById(R.id.name_my_event);
            time = (TextView) itemView.findViewById(R.id.time_my_event);
            imageJoin = (CheckBox) itemView.findViewById(R.id.join_event);
            rating = (TextView) itemView.findViewById(R.id.rating_my_event);
            myEvent = (TextView)itemView.findViewById(R.id.my_event);
            budget = (TextView) itemView.findViewById(R.id.my_event_budget);


        }


    }
}
