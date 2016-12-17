package com.example.polina.meethere.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.polina.meethere.R;

import java.util.List;

/**
 * Created by polina on 11.11.16.
 */

public class ProfileHeaderAdapter extends RecyclerView.Adapter<ProfileHeaderAdapter.ViewHolder>  {

    List<String > categories;

    public ProfileHeaderAdapter(List<String> category) {
        this.categories = category;
    }

    @Override
    public ProfileHeaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category, parent, false);
        return new ProfileHeaderAdapter.ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ProfileHeaderAdapter.ViewHolder holder, int position) {
        String category = categories.get(position);
        holder.categoryButton.setText(category);
        holder.categoryButton.setTag(position);


    }

    @Override
    public int getItemCount() {
        return  (null!=categories? categories.size(): 0);
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        Button categoryButton;
        public ViewHolder(View itemView) {
            super(itemView);
            categoryButton = (Button) itemView.findViewById(R.id.button_category);
        }
    }
}
