package com.tolpa.adapters;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.tolpa.R;
import com.tolpa.Utils;
import com.tolpa.activities.EventActivity;
import com.tolpa.activities.ListOfEventsActivity;
import com.tolpa.model.*;
import com.tolpa.model.Event;

/**
 * Created by polina on 08.03.16.
 */
public class HorizontalEventAdapter extends CursorRecyclerAdapter<RecyclerView.ViewHolder> {

    private static final int ID = 1;
    private static final int NAME = 2;
    private static final int DESCRIPTION = 3;
    private static final int JOINED = 6;
    private static final int START = 4;
    private static final int BUDGET = 8;
    private static final int LAT= 9;
    private static final int LNG= 10;
    private static final int ADDRESS= 7;

    private static final int ATTENDENCE = 11;
    private static final int IMAGE_URL = 12;

    private Activity context;
    private static final int FOOTER_VIEW = 3223;
   int category;
    static final int CHANGE_EVENT_REQUEST = 1;

    public HorizontalEventAdapter(Activity context, int category) {
        super(null);
        this.category = category;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == FOOTER_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
            FooterViewHolder vh = new FooterViewHolder(v);
            return vh;
        }
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event, parent, false);
            ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getCursor().getCount()) {

            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }





    public class FooterViewHolder extends RecyclerView.ViewHolder {



        public FooterViewHolder(View itemView) {
            super(itemView);
            ImageView image = ((ImageView)  itemView.findViewById(R.id.footer));
         //   image.setImageResource(Utils.categoryImage(context, category));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ListOfEventsActivity.class);
                    intent.putExtra(Utils.CATEGORY, category);
                    context.startActivity(intent);
                }
            });
        }
    }


    @Override
    public void onBindViewHolderCursor(RecyclerView.ViewHolder h, Cursor cursor) {
        if (h instanceof ViewHolder) {
            ViewHolder  holder = (ViewHolder) h;
            holder.text.setText(cursor.getString(NAME));
            holder.date.setText(Utils.parseDataDate(cursor.getString(START)));
            holder.time.setText(Utils.parseDataTime(cursor.getString(START)));
            boolean checked = Boolean.parseBoolean(cursor.getString(JOINED));
            holder.budget.setText((cursor.getInt(BUDGET)==0?context.getString(R.string.free): (cursor.getInt(BUDGET))+" "+context.getString(R.string.hrn)));
            holder.attendance.setText("" + cursor.getInt(ATTENDENCE));
            holder.joined.setVisibility(View.GONE);
            if(checked) holder.joined.setVisibility(View.VISIBLE);
            holder.setItemPosition(cursor.getPosition());
            String url = cursor.getString(IMAGE_URL);
            holder.image.setImageURI(Uri.parse(url));
        }
    }

    @Override
    public int getItemCount() {
        if (getCursor() == null) {
            return 0;
        }

        if (getCursor().getCount() == 0) {
            //Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return getCursor().getCount() + 1;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView text;
        private TextView time;
        private TextView date;

       private ImageView joined;
       private TextView budget;
       private TextView attendance;
        int itemPosition;

        public void setItemPosition(int itemPosition) {
            this.itemPosition = itemPosition;
        }

        @Override
        public void onClick(View v) {
            System.out.println(itemPosition);
            Cursor cursor = getCursor();
            cursor.moveToPosition(itemPosition);
            Intent intent = new Intent(context, EventActivity.class);
            String id = cursor.getString(ID);
            intent.putExtra(Utils.EVENT_ID,id);
            intent.putExtra(Utils.EVENT_NAME, cursor.getString(NAME));
            intent.putExtra(Event.DESCRIPTION, cursor.getString(DESCRIPTION));
            intent.putExtra(Event.START, cursor.getString(START));
            intent.putExtra(Event.BUDGET_MIN, cursor.getInt(BUDGET));
            intent.putExtra(Event.ATTENDANCES, cursor.getInt(ATTENDENCE));
            intent.putExtra(Event.JOINED, Boolean.parseBoolean(cursor.getString(JOINED)) );
            intent.putExtra(Event.ADDRESS, cursor.getString(ADDRESS));
            intent.putExtra(Event.LAT, cursor.getDouble(LAT));
            intent.putExtra(Event.LNG, cursor.getDouble(LNG));
            intent.putExtra(Utils.IMAGE_URL, cursor.getString(IMAGE_URL));
            context.startActivityForResult(intent, 10101);
        }


        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.event_image);
            image.setOnClickListener(this);
            text =(TextView)itemView.findViewById(R.id.event_name);
            time = (TextView)itemView.findViewById(R.id.event_time);
            date = (TextView)itemView.findViewById(R.id.event_date);
            budget = (TextView)itemView.findViewById(R.id.event_budget);
            attendance = (TextView)itemView.findViewById(R.id.event_attendence);
           joined = (ImageView) itemView.findViewById(R.id.check_join);


        }
    }

}
