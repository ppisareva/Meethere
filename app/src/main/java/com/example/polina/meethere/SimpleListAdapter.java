package com.example.polina.meethere;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.polina.meethere.adapters.SimpleItem;
import com.example.polina.meethere.activities.ListOfEventsActivity;

import java.util.List;

/**
 * Created by polina on 11.04.16.
 */
public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListAdapter.ViewHolder>{
    List<SimpleItem> simpleItems;
    Context context;

    public SimpleListAdapter(List<SimpleItem> allCategories) {
        this.simpleItems = allCategories;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_view, parent, false);
        context = parent.getContext();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleItem category = simpleItems.get(position);
        holder.text.setText(category.getName());
        holder.icon.setImageResource(category.getImage());
        holder.setPosition(position);

    }

    @Override
    public int getItemCount() {
        return (simpleItems !=null? simpleItems.size():0);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView icon;
        TextView text;
        LinearLayout layout;
        int position;

        public void setPosition(int position) {
            this.position = position;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.simple_icon);
            text = (TextView) itemView.findViewById(R.id.simple_text);
            layout = (LinearLayout) itemView.findViewById(R.id.simpleLayout);
            layout.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            System.out.println(position);
            Intent intent = new Intent(context, ListOfEventsActivity.class);
            intent.putExtra(Utils.CATEGORY, position);
            context.startActivity(intent);
        }
    }

    }
