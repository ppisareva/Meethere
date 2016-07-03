package com.example.polina.meethere;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

/**
 * Created by polina on 10.04.16.
 */
public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> implements View.OnClickListener {

    List<String > categories;

    public HeaderAdapter(List<String> category) {
        this.categories = category;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       String category = categories.get(position);
        holder.categoryButton.setText(category);
        holder.categoryButton.setTag(position);


    }

    @Override
    public int getItemCount() {
        return  (null!=categories? categories.size(): 0);
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        System.err.println("On click " + position);
        if (position == getItemCount()-1) {
            v.getContext().startActivity(new Intent(v.getContext(), CategoryListActivity.class));
        } else {
            Intent intent = new Intent(v.getContext(), ListOfEventsActivity.class);
            intent.putExtra(Utils.CATEGORY, position);
            v.getContext().startActivity(intent);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        Button categoryButton;
        public ViewHolder(View itemView) {
            super(itemView);
            categoryButton = (Button) itemView;
        }
    }
}
