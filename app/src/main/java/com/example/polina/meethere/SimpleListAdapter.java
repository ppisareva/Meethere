package com.example.polina.meethere;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polina.meethere.Adapters.SimpleItem;

import java.util.List;

/**
 * Created by polina on 11.04.16.
 */
public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListAdapter.ViewHolder>{
    List<SimpleItem> simpleItems;

    public SimpleListAdapter(List<SimpleItem> allCategories) {
        this.simpleItems = allCategories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleItem category = simpleItems.get(position);
        holder.text.setText(category.getName());
        holder.icon.setImageResource(category.getImage());

    }

    @Override
    public int getItemCount() {
        return (simpleItems !=null? simpleItems.size():0);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.simple_icon);
            text = (TextView) itemView.findViewById(R.id.simple_text);
        }
    }
}
