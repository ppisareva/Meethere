package com.tolpa.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tolpa.R;
import com.tolpa.Utils;
import com.tolpa.activities.UserProfileActivity;
import com.tolpa.model.App;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by polina on 13.07.16.
 */
public class MyFriendsAdapter extends CursorRecyclerAdapter<MyFriendsAdapter.ViewHolder > {

    private static final int ID = 0;
    Context context;
    private static final int NAME = 1;
    private static final int LAST_NAME = 2;
    private static final int URL = 3;
    App app;

    public MyFriendsAdapter(Context context) {
        super(null);
        this.context = context;
        app = (App)context.getApplicationContext();

    }

    @Override
    public void onBindViewHolderCursor(MyFriendsAdapter.ViewHolder holder, Cursor cursor) {
        if(cursor.getInt(ID)==app.getUserProfile().getId()) {
            holder.name.setText(cursor.getString(NAME) + " " + cursor.getString(LAST_NAME) + " (Я)");
            holder.linearLayout.setClickable(false);
        } else {
            holder.name.setText(cursor.getString(NAME) + " " + cursor.getString(LAST_NAME));
        }
        holder.setID(cursor.getInt(ID));
        holder.image.setImageURI(Uri.parse(cursor.getString(URL)));
        RoundingParams roundingParams = RoundingParams.asCircle();
        holder.image.getHierarchy().setRoundingParams(roundingParams);
    }

    @Override
    public MyFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends, parent, false);
        MyFriendsAdapter.ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private SimpleDraweeView image;
        private TextView name;
        private LinearLayout linearLayout;

        int id;

        public void setID(int id) {
            this.id = id;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.friend_name);
            image= (SimpleDraweeView)itemView.findViewById(R.id.friend_image);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.friend_layout);
            linearLayout.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            System.out.println(id);
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra(Utils.USER_ID, id);
            context.startActivity(intent);
        }
    }
}
