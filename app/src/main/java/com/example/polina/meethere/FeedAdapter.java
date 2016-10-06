package com.example.polina.meethere;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.internal.Utility;

/**
 * Created by polina on 27.07.16.
 */
public class FeedAdapter extends CursorRecyclerAdapter<FeedAdapter.ViewHolder > {
    public static final String IMG_PATTERN = "https://s3-us-west-1.amazonaws.com/meethere/%s.jpg";
    private static final int ID = 0;
    private static final int LAST_NAME = 1;
    private static final int FIRST_NAME = 2;
    private static final int IMAGE = 3;
    private static final int ID_USER = 4;
    private static final int TYPE = 5;
    private static final int TIME = 6;
    private static final int EVENT_START = 7;
    private static final int EVENT_BUDGET = 8;
    private static final int EVENT_ID = 9;
    private static final int EVENT_NAME = 10;
    private static final int EVENT_DESCRIPTION = 11;
    Context context;



    public FeedAdapter(Context context) {
        super(null);
        this.context=context;
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {

        String uri = String.format(IMG_PATTERN, cursor.getString(EVENT_ID));
        holder.eventImage.setImageURI(Uri.parse(uri));
       // holder.eventImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.userPhoto.setImageURI(Uri.parse(cursor.getString(IMAGE)));
        holder.userName.setText( cursor.getString(FIRST_NAME) + " " + cursor.getString(LAST_NAME));
        switch (cursor.getString(TYPE)){
            case Utils.CREATE_EVENT:
                holder.action.setText(context.getString(R.string.created_event));
                break;
            case Utils.INVITE:
                holder.action.setText(context.getString(R.string.invite));
                break;
            case Utils.JOIN:
                holder.action.setText(context.getString(R.string.join));
                break;
        }

        String time = Utils.parseData(cursor.getString(TIME));
        holder.actionTime.setText(time);
         time = Utils.parseData(cursor.getString(EVENT_START));
        holder.eventTime.setText(time);

        holder.eventBudget.setText(cursor.getString(EVENT_BUDGET)+  " "+context.getString(R.string.hrn));
        holder.eventName.setText(cursor.getString(EVENT_NAME));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_layout, parent, false);
        FeedAdapter.ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView layout;
        SimpleDraweeView userPhoto;
        TextView userName;
        TextView actionTime;
        TextView action;
        SimpleDraweeView eventImage;
        TextView eventName;
        TextView eventTime;
        TextView eventBudget;
        int userId;
        int eventId;

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            layout = (CardView) itemView.findViewById(R.id.feed);
            userPhoto = (SimpleDraweeView) itemView.findViewById(R.id.profile_image);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            actionTime = (TextView) itemView.findViewById(R.id.action_time);
            action = (TextView) itemView.findViewById(R.id.action);
            eventImage = (SimpleDraweeView) itemView.findViewById(R.id.event_image);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            eventTime = (TextView)itemView.findViewById(R.id.event_time);
            eventBudget = (TextView)itemView.findViewById(R.id.event_budget);
        }
    }

}
