package com.example.polina.meethere;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.polina.meethere.Adapters.Event;

import java.util.List;

/**
 * Created by polina on 08.03.16.
 */
public class HorizontalEventAdapter extends RecyclerView.Adapter<HorizontalEventAdapter.ViewHolder> {

    private Activity context;
    private List<Event> eventList;



    public HorizontalEventAdapter(Activity context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public HorizontalEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event, parent, false);
            ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.text.setText(event.getName());
        holder.setItemPosition(position);
        try {
            holder.image.setImageResource(event.getPhoto());
        }catch (Throwable t) {}
    }

    @Override
    public int getItemCount() {
        return    (null!=eventList? eventList.size(): 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView text;
        int itemPosition;

        public void setItemPosition(int itemPosition) {
            this.itemPosition = itemPosition;
        }

        @Override
        public void onClick(View v) {
            Event item = eventList.get(itemPosition);
            System.out.println(itemPosition);
            Intent intent = new Intent(context, EventActivity.class);
            context.startActivity(intent);
        }


        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.event_image);
            image.setOnClickListener(this);
            text =(TextView)itemView.findViewById(R.id.event_name);
        }
    }

}
