package com.example.polina.meethere;

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


import com.example.polina.meethere.model.Event;

/**
 * Created by polina on 08.03.16.
 */
public class HorizontalEventAdapter extends CursorRecyclerAdapter<HorizontalEventAdapter.ViewHolder> {

    private static final int ID = 0;
    private static final int NAME = 1;
    private static final int DESCRIPTION = 2;
    private Activity context;

    public static final String IMG_PATTERN = "https://s3-us-west-1.amazonaws.com/meethere/%s.jpg";


    public HorizontalEventAdapter(Activity context) {
        super(null);
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
            String id = cursor.getString(ID);
            String url = String.format(IMG_PATTERN, id);
            holder.image.setImageURI(Uri.parse(url));
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
            Cursor cursor = getCursor();
            cursor.moveToPosition(itemPosition);
            Intent intent = new Intent(context, EventActivity.class);
            String id = cursor.getString(ID);
            intent.putExtra(Utils.EVENT_ID,id );
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
