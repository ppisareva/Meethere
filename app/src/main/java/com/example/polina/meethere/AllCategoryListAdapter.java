package com.example.polina.meethere;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polina.meethere.Adapters.AllCategory;

import java.util.List;

/**
 * Created by polina on 11.04.16.
 */
public class AllCategoryListAdapter  extends RecyclerView.Adapter<AllCategoryListAdapter.ViewHolder>{
    List<AllCategory> allCategories;

    public AllCategoryListAdapter(List<AllCategory> allCategories) {
        this.allCategories = allCategories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_category, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AllCategory category = allCategories.get(position);
        holder.category.setText(category.getCategory());
        holder.icon.setImageResource(category.getImage());

    }

    @Override
    public int getItemCount() {
        return (allCategories!=null? allCategories.size():0);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView category;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.all_category_icon);
            category = (TextView) itemView.findViewById(R.id.all_category_category);
        }
    }
}
