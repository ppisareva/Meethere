package com.example.polina.meethere;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.polina.meethere.activities.EventActivity;
import com.example.polina.meethere.activities.ListOfEventsActivity;

/**
 * Created by polina on 08.03.16.
 */
public class HorizontalEventAdapter extends CursorRecyclerAdapter<RecyclerView.ViewHolder> {

    private static final int ID = 1;
    private static final int NAME = 2;
    private static final int JOINED = 6;
    private static final int START = 4;
    private static final int BUDGET = 8;
    private static final int ATTENDENCE = 11;

    private Activity context;
    private static final int FOOTER_VIEW = 3223;
   int category;
    static final int CHANGE_EVENT_REQUEST = 1;

    public static final String IMG_PATTERN = "https://s3-us-west-1.amazonaws.com/meethere/%s.jpg";


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
    public Cursor getCursor() {
        return super.getCursor();
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

        try {
            if (h instanceof ViewHolder) {
                ViewHolder  holder = (ViewHolder) h;
                holder.text.setText(cursor.getString(NAME));
                holder.date.setText(Utils.parseDataDate(cursor.getString(START)));
                holder.time.setText(Utils.parseDataTime(cursor.getString(START)));
                boolean checked = Boolean.parseBoolean(cursor.getString(JOINED));
                holder.budget.setText((cursor.getInt(BUDGET)==0?context.getString(R.string.free): (cursor.getInt(BUDGET))+" "+context.getString(R.string.hrn)));
                holder.attendance.setText(cursor.getInt(ATTENDENCE)+"");
                holder.joined.setVisibility(View.GONE);
                if(checked) holder.joined.setVisibility(View.VISIBLE);
                holder.setItemPosition(cursor.getPosition());
                String id = cursor.getString(ID);
                String url = String.format(IMG_PATTERN, id);
                holder.image.setImageURI(Uri.parse(url));
            }

        } catch (Exception e){

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

            context.startActivity(intent);
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
