package com.example.polina.meethere;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.polina.meethere.adapters.Category;
import com.example.polina.meethere.activities.ListOfEventsActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by polina on 09.03.16.
 */
public class VerticalEventAdapter extends RecyclerView.Adapter<VerticalEventAdapter.MyViewHolder> {

    List<Category> categoryList;
    Activity context;
    final Map<Integer, HorizontalEventAdapter> adapters = new HashMap();
    boolean flag = true;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public VerticalEventAdapter(List<Category> categoryList, Activity context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iner_list, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    private CursorRecyclerAdapter getAdapter(int categoryId) {
        if (!adapters.containsKey(categoryId))
            adapters.put(categoryId, new HorizontalEventAdapter(context, categoryId));
        return adapters.get(categoryId);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.textView.setText(category.getName());
        holder.setCategory(category.getId());
        System.err.println("EVENT LIST: " + category.getName());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(getAdapter(category.getId()));
        holder.recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return  (null!= categoryList ? categoryList.size(): 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        RecyclerView recyclerView;
        RecyclerViewHeader header;

        TextView textView;
        private int category;



        public void setCategory (int itemPosition) {
            this.category = itemPosition;
        }

        public MyViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.horizontal_list);



            textView = (TextView) itemView.findViewById(R.id.list_category);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ListOfEventsActivity.class);
            intent.putExtra(Utils.CATEGORY, category);
            context.startActivity(intent);
        }
    }

    public void updateCursor(int categoryId, Cursor cursor) {
        getAdapter(categoryId).swapCursor(cursor);
        System.err.println("UPDATED cursor for category #" + categoryId);
    }
}
