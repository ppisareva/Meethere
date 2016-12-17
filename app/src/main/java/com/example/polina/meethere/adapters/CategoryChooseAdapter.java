package com.example.polina.meethere.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.polina.meethere.R;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by polina on 12.05.16.
 */
public class CategoryChooseAdapter extends RecyclerView.Adapter<CategoryChooseAdapter.ViewHolder> {

    List<String> list;
    Set<Integer> checked  = new HashSet<>();
    private CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position =(int) buttonView.getTag();
            if(isChecked) checked.add(position);
            else checked.remove(position);

        }
    };;

    public CategoryChooseAdapter(List<String> list, Set<Integer> checked) {
        this.list = list;
        this.checked = checked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View v =   LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_choose, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.category.setText(list.get(position));
        holder.checkBox.setTag(position);
        holder.checkBox.setChecked(checked.contains(position));
        holder.checkBox.setOnCheckedChangeListener(changeListener);

    }

    @Override
    public int getItemCount() {
        return (list !=null? list.size():0);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView category;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.category_choose_text);
            checkBox = (CheckBox) itemView.findViewById(R.id.check_category);

        }

    }

    public Collection<Integer> getTags (){
        return checked;
    }
}
