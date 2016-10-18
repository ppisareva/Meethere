package com.example.polina.meethere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
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
    private static final int JOINED = 5;
    private static final int BUDGET = 7;
    private static final int ATTENDANCE = 10;



    private static final int CHANGE_EVENT_REQUEST = 7;





    public static final String IMG_PATTERN = "https://s3-us-west-1.amazonaws.com/meethere/%s.jpg";

    public MyEventsAdapter(Context context) {
        super(null);
        this.context = context;
    }

    @Override
    public MyEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_view, parent, false);
        MyEventsAdapter.ViewHolder vh = new ViewHolder(v);
    //    vh.image.setColorFilter(0x77ffffff & parent.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.DARKEN);
        return vh;
    }



    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {
        holder.name.setText(cursor.getString(NAME));
        holder.time.setText(Utils.parseDataTime(cursor.getString(START)));
        holder.date.setText(Utils.parseDataDate(cursor.getString(START)));
        boolean checked = Boolean.parseBoolean(cursor.getString(JOINED));

        holder.budget.setText(cursor.getInt(BUDGET)==0?"Бесплатно":(cursor.getInt(BUDGET)+ " грн"));
        holder.people.setText(cursor.getInt(ATTENDANCE) +"");
        holder.imageJoin.setVisibility(View.GONE);
        if(checked) holder.imageJoin.setVisibility(View.VISIBLE);
        holder.setID(cursor.getString(ID));
        holder.setUserName(cursor.getString(NAME));
        String url = String.format(IMG_PATTERN, cursor.getString(ID));
        holder.image.setImageURI(Uri.parse(url));
//        PorterDuff.Mode[] values = PorterDuff.Mode.values();
//        holder.image.setColorFilter(0x7700ff00, values[cursor.getPosition()%values.length]);
//        System.err.println(values[cursor.getPosition()%values.length] + " || " + cursor.getString(NAME) + " " + Utils.parseData(cursor.getString(START)));

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private ImageView image;
        private ImageView imageJoin;
        private TextView name;
        private TextView date;
        private TextView people;
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
            imageJoin = (ImageView) itemView.findViewById(R.id.check_join);
          date = (TextView) itemView.findViewById(R.id.date);
           budget = (TextView) itemView.findViewById(R.id.budget_myevent);
           people = (TextView) itemView.findViewById(R.id.myevent_people);

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
