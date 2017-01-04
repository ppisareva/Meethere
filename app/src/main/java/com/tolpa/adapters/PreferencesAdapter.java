package com.tolpa.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tolpa.R;

import java.util.List;

/**
 * Created by polina on 04.05.16.
 */
public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.ViewHolder>  {
    List<String> list;

    public PreferencesAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.preferences, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(list.get(position));

    }

    @Override
    public int getItemCount() {

            return  (null!=list? list.size(): 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;



        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.preferences);
        }
    }
}
