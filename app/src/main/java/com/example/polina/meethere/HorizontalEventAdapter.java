package com.example.polina.meethere;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.polina.meethere.model.Event;

import java.util.List;

/**
 * Created by polina on 08.03.16.
 */
public class HorizontalEventAdapter extends CursorRecyclerAdapter<HorizontalEventAdapter.ViewHolder> {

    private static final int NAME = 1;
    private Activity context;



    public HorizontalEventAdapter(Activity context, Cursor eventList) {
        super(eventList);
        this.context = context;
    }

    @Override
    public HorizontalEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event, parent, false);
            ViewHolder vh = new ViewHolder(v);
        return vh;
    }




    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {
            holder.text.setText(cursor.getString(NAME));
            holder.setItemPosition(cursor.getPosition());
            holder.image.setImageResource(R.drawable.cappuccino);
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
