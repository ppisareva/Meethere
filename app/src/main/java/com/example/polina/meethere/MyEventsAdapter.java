package com.example.polina.meethere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.polina.meethere.activities.EventActivity;
import com.example.polina.meethere.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by polina on 18.03.16.
 */
public class MyEventsAdapter extends CursorRecyclerAdapter<MyEventsAdapter.ViewHolder >  {

    private static final int ID = 0;
    Context context;
    private static final int NAME = 1;
    private static final int START = 3;
    private static final int PLACE = 6;
    private static final int ADDRESS = 7;
    private static final int CHANGE_EVENT_REQUEST = 7;





    public static final String IMG_PATTERN = "https://s3-us-west-1.amazonaws.com/meethere/%s.jpg";

    public MyEventsAdapter(Context context) {
        super(null);
        this.context = context;
    }

    @Override
    public MyEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_event, parent, false);
        MyEventsAdapter.ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {



        holder.name.setText(cursor.getString(NAME));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss");
        Date dateStart = null;
        try {
            dateStart = simpleDateFormat.parse(cursor.getString(START));
            if(dateStart==null){
                simpleDateFormat = new SimpleDateFormat("[\"yyyy-MM-dd'T'kk:mm:ss\"]");
                dateStart = simpleDateFormat.parse(cursor.getString(START));

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        simpleDateFormat = new SimpleDateFormat("EEE, MM dd kk:mm");




        holder.time.setText(simpleDateFormat.format(dateStart));

        holder.setID(cursor.getString(ID));
        holder.setUserName(cursor.getString(NAME));
        String url = String.format(IMG_PATTERN, cursor.getString(ID));
        holder.image.setImageURI(Uri.parse(url));
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private ImageView image;
        private CheckBox imageJoin;
        private TextView name;
        private TextView rating;
        private TextView myEvent;
        private TextView budget;
        private TextView time;
        private RelativeLayout cardView;
        String id;
        String userName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setID(String id) {
            this.id = id;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image_my_event);
            name = (TextView) itemView.findViewById(R.id.name_my_event);
            cardView = (RelativeLayout) itemView.findViewById(R.id.card);
            cardView.setOnClickListener(this);
            time = (TextView) itemView.findViewById(R.id.time_my_event);
            imageJoin = (CheckBox) itemView.findViewById(R.id.join_event);
            rating = (TextView) itemView.findViewById(R.id.address_myevent);
            myEvent = (TextView)itemView.findViewById(R.id.my_event);
            budget = (TextView) itemView.findViewById(R.id.my_event_budget);
        }


        @Override
        public void onClick(View v) {
            System.out.println(id);
            Intent intent = new Intent(context, EventActivity.class);
            intent.putExtra(Utils.EVENT_ID, id);
            intent.putExtra(Utils.EVENT_NAME, getUserName());
            ((Activity) context).startActivityForResult(intent, CHANGE_EVENT_REQUEST);
        }
    }
}
